package com.practice.fullstackbackendspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class FullstackBackendSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(FullstackBackendSpringbootApplication.class, args);
	}

}
