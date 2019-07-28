package com.fooock.robotstxt.database.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource


/**
 *
 */
@EnableTransactionManagement
@EnableJpaAuditing
@PropertySource("classpath:db.properties")
@Configuration
class DataSourceConfiguration(private val env: Environment) {

    @Bean
    fun dataSource(): DataSource {
        val builder = DriverManagerDataSource()
        builder.username = env.getProperty("spring.datasource.username")
        builder.password = env.getProperty("spring.datasource.password")
        builder.url = env.getProperty("spring.datasource.url")
        builder.setDriverClassName(env.getProperty("spring.datasource.driverClassName", ""))
        return builder
    }
}
