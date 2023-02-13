package com.shop.prices.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

@Configuration
public class DateConfig {

    @Bean
    public SimpleDateFormat dateFormat(@Value("${application.date_format}") String format) {
        return new SimpleDateFormat(format);
    }

}
