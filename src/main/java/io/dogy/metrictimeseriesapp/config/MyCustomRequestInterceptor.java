package io.dogy.metrictimeseriesapp.config;

import com.adobe.aam.metrics.agent.ImmutableMetricAgentConfig;
import com.adobe.aam.metrics.agent.MetricAgent;
import com.adobe.aam.metrics.agent.MetricAgentConfig;
import com.adobe.aam.metrics.metric.CounterMetric;
import com.adobe.aam.metrics.metric.Metric;
import com.adobe.aam.metrics.metric.MetricLabels;
import io.dogy.metrictimeseriesapp.metric.TimescaleDbMetricClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
public class MyCustomRequestInterceptor extends HandlerInterceptorAdapter {

    private final CounterMetric counterMetric = new CounterMetric(MetricLabels.of("public.request_counter"));
    private final Metric averageMetric = Metric.newInstance("public.request_time", Metric.Type.AVG);

    @Autowired
    public MyCustomRequestInterceptor(TimescaleDbMetricClient metricClient) {
        metricClient.createMetricIfNotExist(counterMetric.getName());
        metricClient.createMetricIfNotExist(averageMetric.getName());

        MetricAgentConfig metricAgentConfig = ImmutableMetricAgentConfig.builder()
                .collectFrequency(Duration.ofSeconds(15))
                .addMetrics(counterMetric, averageMetric)
                .tags(metricClient.getConfig().tags())
                .build();

        MetricAgent metricAgent = new MetricAgent(metricClient, metricAgentConfig);
        metricAgent.startAsync();

        Runtime.getRuntime().addShutdownHook(new Thread(metricAgent::stopAsync));
    }

    public static String makeUrl(HttpServletRequest request) {
        StringBuilder builder = new StringBuilder(request.getRequestURL().toString());
        if (request.getQueryString() != null && !request.getQueryString().trim().isEmpty()) {
            builder.append("?").append(request.getQueryString());
        }
        return builder.toString();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long startTime = Instant.now().toEpochMilli();
        System.out.println("\n[" + request.getMethod() + "] Request URL:: " + makeUrl(request) + "\n :: Start Time=" + new Date());
        request.setAttribute("startTime", startTime);

        counterMetric.increment();

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) {
        long startTime = (Long) request.getAttribute("startTime");
        long latency = Instant.now().toEpochMilli() - startTime;
        System.out.println(" :: Time Taken=" + latency);

        averageMetric.track(latency);
    }
}
