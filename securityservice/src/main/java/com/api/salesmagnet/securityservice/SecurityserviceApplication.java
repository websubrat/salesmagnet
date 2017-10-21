package com.api.salesmagnet.securityservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SecurityserviceApplication {

	@RequestMapping("security/hello")
    String hello() {
        return "Hello I will keep this applicaiton safe!!";
    }
	
	public static void main(String[] args) {
		SpringApplication.run(SecurityserviceApplication.class, args);
	}
}
