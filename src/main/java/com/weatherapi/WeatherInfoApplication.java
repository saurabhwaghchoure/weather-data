package com.weatherapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.weatherapi.dao")
@EntityScan(basePackages = "com.weatherapi.entities")
public class WeatherInfoApplication {
    public static void main(String[] args) {
        SpringApplication.run(WeatherInfoApplication.class, args);
    }
}
