package com.api.salesmagnet.contactservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class ContactserviceApplication {
	
	@RequestMapping("contacts/hello")
    String hello() {
        return "Hello contact service!!";
    }

	public static void main(String[] args) {
		SpringApplication.run(ContactserviceApplication.class, args);
	}
}
