package com.example.noti_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NotiServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotiServiceApplication.class, args);
	}

}
