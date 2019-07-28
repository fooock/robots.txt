package com.fooock.robotstxt.crawlapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 *
 */
@EntityScan("com.fooock.robotstxt")
@EnableJpaRepositories("com.fooock.robotstxt.database")
@ComponentScan("com.fooock.robotstxt")
@SpringBootApplication
public class CrawlApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(CrawlApiApplication.class, args);
    }
}
