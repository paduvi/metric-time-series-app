package io.dogy.metrictimeseriesapp.metric;

import com.adobe.aam.metrics.core.config.ImmutablePublisherConfig;
import com.adobe.aam.metrics.metric.ImmutableTags;
import io.dogy.metrictimeseriesapp.config.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricConfig {

    @Bean
    public ImmutablePublisherConfig publisherConfig() {
        return ImmutablePublisherConfig.builder()
                .host("https://dogy.io")
                .name("TimescaleDB publisher")
                .type("TimescaleDB")
                .sendOnlyRecentlyUpdatedMetrics(true)
                .tags(ImmutableTags.builder().hostname(Constants.HOST_NAME).build())
                .build();
    }

}
