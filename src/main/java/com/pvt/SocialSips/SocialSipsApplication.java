package com.pvt.SocialSips;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SocialSipsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialSipsApplication.class, args);
	}

	@GetMapping
	public String hello() {
		return "Hello world!";
	}
}
