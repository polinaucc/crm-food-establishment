package com.crm.food.establishment.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@ConfigurationPropertiesScan

//TODO add tests for logs
public class UserManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserManagerApplication.class, args);
	}

}
