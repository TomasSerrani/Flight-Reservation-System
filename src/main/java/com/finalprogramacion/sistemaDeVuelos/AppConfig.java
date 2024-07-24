package com.finalprogramacion.sistemaDeVuelos;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "com.finalprogramacion.sistemaDeVuelos")
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/ticketSystem");
        dataSource.setUsername("postgres");
        dataSource.setPassword("tomas2004");
        return dataSource;
    }

}
