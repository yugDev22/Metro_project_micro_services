package com.card;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableEurekaClient
@EnableJpaRepositories(basePackages = "com.card.persistence")
@EntityScan(basePackages = "com.card.bean")
public class MetrocardServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MetrocardServiceApplication.class, args);
	}

}
