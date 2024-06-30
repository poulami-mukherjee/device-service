package com.example.deviceservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class DeviceManagementServiceApplication {

	public static void main(String[] args) {
		log.info("DeviceManagementService Application Starting up");
		SpringApplication.run(DeviceManagementServiceApplication.class, args);
	}
}
