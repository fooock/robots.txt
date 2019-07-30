package com.fooock.robotstxt.api.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Configure application dependencies
 */
@EntityScan("com.fooock.robotstxt")
@EnableJpaRepositories("com.fooock.robotstxt.database")
@ComponentScan("com.fooock.robotstxt")
@Configuration
public class AppConfiguration {
    // See https://github.com/spring-projects/spring-boot/issues/6844
}
