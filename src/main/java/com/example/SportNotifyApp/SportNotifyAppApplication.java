package com.example.SportNotifyApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SportNotifyAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SportNotifyAppApplication.class, args);
	}

}
