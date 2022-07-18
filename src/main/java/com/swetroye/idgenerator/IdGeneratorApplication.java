package com.swetroye.idgenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IdGeneratorApplication {
	long datacenterId;

	public static void main(String[] args) {
		SpringApplication.run(IdGeneratorApplication.class, args);

	}

}
