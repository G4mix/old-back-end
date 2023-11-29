package com.gamix.config;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(System.getenv("SPRING_DATASOURCE_DRIVER_CLASS_NAME"));
        dataSource.setUrl(System.getenv("SPRING_DATASOURCE_URL"));
        dataSource.setUsername(System.getenv("SPRING_DATASOURCE_USERNAME"));
        dataSource.setPassword(System.getenv("SPRING_DATASOURCE_PASSWORD"));
        return dataSource;
    }
}
