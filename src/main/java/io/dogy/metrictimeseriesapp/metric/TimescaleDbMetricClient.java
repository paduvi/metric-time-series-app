package io.dogy.metrictimeseriesapp.metric;

import com.adobe.aam.metrics.BufferedMetricClient;
import com.adobe.aam.metrics.core.config.PublisherConfig;
import com.adobe.aam.metrics.metric.Metric;
import lombok.SneakyThrows;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class TimescaleDbMetricClient implements BufferedMetricClient {

    private final LinkedBlockingQueue<Metric> queue = new LinkedBlockingQueue<>();
    private final PublisherConfig config;
    private final BasicDataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TimescaleDbMetricClient(PublisherConfig config, BasicDataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.config = config;
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    public PublisherConfig getConfig() {
        return config;
    }

    public void createMetricIfNotExist(String metricName) {
        // create table if not exists
        jdbcTemplate.execute(String.format(
                "CREATE TABLE IF NOT EXISTS %s (" +
                        "timestamp      TIMESTAMPTZ         NOT NULL," +
                        "host           TEXT                NOT NULL," +
                        "value          DOUBLE PRECISION    NULL" +
                        ")",
                metricName));

        // create hypertable if not exists
        jdbcTemplate.execute(String.format(
                "SELECT create_hypertable('%s', 'timestamp', if_not_exists => TRUE)",
                metricName));

        // Automatic Data Retention
        jdbcTemplate.execute(String.format(
                "SELECT add_drop_chunks_policy('%s', INTERVAL '30 days', if_not_exists => TRUE)",
                metricName));
    }

    @SneakyThrows
    @Override
    public void shutdown() {
        this.dataSource.close();
    }

    @Override
    public void send(Metric metric) {
        System.out.println("add metric: " + metric.toString());
        queue.add(metric);
    }

    @Override
    public void send(Collection<Metric> collection) {
        System.out.println("add metrics: " + collection.toString());
        queue.addAll(collection);
    }

    @Override
    public void flush() {
        List<Metric> collection = new LinkedList<>();
        queue.drainTo(collection);

        System.out.println("Flush...");
        for (Metric metric : collection) {
            System.out.println(metric.getName() + " - " + metric.get());
            String sql = String.format("INSERT INTO %s(timestamp, host, value) VALUES (NOW(), ?, ?)", metric.getName());

            jdbcTemplate.update(sql, ps -> {
                ps.setString(1, config.tags().hostname().get());
                ps.setDouble(2, metric.doGetAndReset());
            });
        }
    }

}
