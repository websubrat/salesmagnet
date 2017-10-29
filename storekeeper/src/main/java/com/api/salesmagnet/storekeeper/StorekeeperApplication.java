package com.api.salesmagnet.storekeeper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
@RefreshScope
@RequestMapping("/storekeeper")
public class StorekeeperApplication {
	
	@Value("${message: Default hello}")
	private String message;
	
	@RequestMapping("/hello")
    String hello() {
        return "Hello Storekeeper!! says"+message;
    }

	public static void main(String[] args) {
		SpringApplication.run(StorekeeperApplication.class, args);
	}
}
