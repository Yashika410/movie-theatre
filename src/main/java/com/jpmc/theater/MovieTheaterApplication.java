package com.jpmc.theater;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class MovieTheaterApplication {
	public static void main(String[] args)  {
		SpringApplication.run(MovieTheaterApplication.class, args);
    }
}
