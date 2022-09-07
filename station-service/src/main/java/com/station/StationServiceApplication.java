package com.station;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableEurekaClient
@EnableJpaRepositories(basePackages = "com.station.persistence")
@EntityScan(basePackages = "com.station.bean")
public class StationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StationServiceApplication.class, args);
	}

}
