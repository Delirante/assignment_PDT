package com.example.pdtProject;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@SpringBootApplication
public class PdtProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(PdtProjectApplication.class, args);
	}
	
	//Initialization of database
	@Bean
	public DataSource dataSource() {
	    DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setDriverClassName("org.postgresql.Driver");
	    dataSource.setUrl("jdbc:postgresql://localhost/gis");
	    dataSource.setUsername("postgres");
	    dataSource.setPassword("admin");
	    return dataSource;
	}
	
	@Bean
	public DatabaseCalls databaseCalls()
	{
		return new DatabaseCalls(dataSource());
	}
}
