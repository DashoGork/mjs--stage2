package com.epam.esm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement(proxyTargetClass = true)
public class SpringConfig implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(SpringConfig.class, args);
    }
}