package com.aevin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class AevinApplication {

	public static void main(String[] args) {
		SpringApplication.run(AevinApplication.class, args);
	}

}

