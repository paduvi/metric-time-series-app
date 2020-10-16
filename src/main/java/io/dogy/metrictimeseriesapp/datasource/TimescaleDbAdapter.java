package io.dogy.metrictimeseriesapp.datasource;

import io.dogy.metrictimeseriesapp.config.Constants;
import org.apache.commons.dbcp2.BasicDataSource;
import org.postgresql.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collections;

@Configuration
public class TimescaleDbAdapter {

    @Bean(destroyMethod = "")
    public BasicDataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        String connectionString = String.format("jdbc:postgresql://%s:%d/%s", Constants.TIMESCALEDB_HOST, Constants.TIMESCALEDB_PORT, Constants.TIMESCALEDB_DATABASE);

        dataSource.setDriverClassName(Driver.class.getName());
        dataSource.setUsername(Constants.TIMESCALEDB_USERNAME);
        dataSource.setPassword(Constants.TIMESCALEDB_PASSWORD);
        dataSource.setUrl(connectionString);
        dataSource.setInitialSize(0);
        dataSource.setMaxTotal(Constants.TIMESCALEDB_POOL_SIZE);

        dataSource.setTestOnBorrow(true);
        dataSource.setTestWhileIdle(true);
        dataSource.setValidationQueryTimeout(3);
        dataSource.setValidationQuery("SELECT 1");
        dataSource.setConnectionInitSqls(Collections.singletonList("SET client_encoding = 'UTF8'"));

        dataSource.setMaxConnLifetimeMillis(900000);
        dataSource.setTimeBetweenEvictionRunsMillis(300000);
        dataSource.setLogExpiredConnections(false);

        return dataSource;
    }

    @Bean(destroyMethod = "")
    public JdbcTemplate jdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());
        jdbcTemplate.setResultsMapCaseInsensitive(false);

        return jdbcTemplate;
    }

}
