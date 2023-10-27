package com.gamix.config;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class DotenvConfig {
    @Bean
    public Dotenv dotenv() {
        return Dotenv.load();
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        Dotenv dotenv = dotenv();
        dataSource.setDriverClassName(dotenv.get("SPRING_DATASOURCE_DRIVER_CLASS_NAME"));
        dataSource.setUrl(dotenv.get("SPRING_DATASOURCE_URL"));
        dataSource.setUsername(dotenv.get("SPRING_DATASOURCE_USERNAME"));
        dataSource.setPassword(dotenv.get("SPRING_DATASOURCE_PASSWORD"));
        return dataSource;
    }
}
