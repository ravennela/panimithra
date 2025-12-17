package com.example.fixmate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class FixmateApplication {

	public static void main(String[] args) {
		SpringApplication.run(FixmateApplication.class, args);
	}

}
