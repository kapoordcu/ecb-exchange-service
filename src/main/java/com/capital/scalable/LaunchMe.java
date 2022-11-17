package com.capital.scalable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LaunchMe {

	public static void main(String[] args) {
		SpringApplication.run(LaunchMe.class, args);
	}

}
