package com.chema.steamfamilymanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SteamFamilyManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SteamFamilyManagerApplication.class, args);
	}

}
