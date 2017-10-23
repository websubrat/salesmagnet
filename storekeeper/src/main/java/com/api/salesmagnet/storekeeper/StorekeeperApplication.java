package com.api.salesmagnet.storekeeper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class StorekeeperApplication {
	
	@RequestMapping("storekeeper/hello")
    String hello() {
        return "Hello Storekeeper!!";
    }

	public static void main(String[] args) {
		SpringApplication.run(StorekeeperApplication.class, args);
	}
}
