package com.springmvcapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
@SpringBootApplication
@EntityScan("com.springmvcapp.entity")
public class SpringMvsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMvsApplication.class, args);
	}

}
