package com.fooock.robotstxt.crawlapi.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Base application configuration
 */
@EntityScan("com.fooock.robotstxt")
@EnableJpaRepositories("com.fooock.robotstxt.database")
@ComponentScan("com.fooock.robotstxt")
@Configuration
public class AppConfiguration {
}
