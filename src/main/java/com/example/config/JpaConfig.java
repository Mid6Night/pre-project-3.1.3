package com.example.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = {"config", "controller"})
@EnableJpaRepositories(basePackages = "repos")
@EntityScan(basePackages = {"entity"})
public class JpaConfig {
}
