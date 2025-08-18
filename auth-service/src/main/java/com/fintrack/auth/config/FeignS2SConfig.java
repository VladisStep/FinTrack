package com.fintrack.auth.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignS2SConfig {

    @Value("${s2s.secret}")
    private String s2sSecret;

    @Value("${s2s.header:X-Internal-Auth}")
    private String s2sHeader;

    @Bean
    public RequestInterceptor s2sInterceptor() {
        return (RequestTemplate template) -> {
            // add a service header to all calls
            template.header(s2sHeader, s2sSecret);
        };
    }
}
