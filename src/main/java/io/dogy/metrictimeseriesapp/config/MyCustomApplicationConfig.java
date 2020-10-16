package io.dogy.metrictimeseriesapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyCustomApplicationConfig implements WebMvcConfigurer {

    private final MyCustomRequestInterceptor customRequestInterceptor;

    @Autowired
    public MyCustomApplicationConfig(MyCustomRequestInterceptor customRequestInterceptor) {
        this.customRequestInterceptor = customRequestInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);

        registry.addInterceptor(customRequestInterceptor);
    }

}
