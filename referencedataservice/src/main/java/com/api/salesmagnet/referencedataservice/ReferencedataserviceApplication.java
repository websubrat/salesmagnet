package com.api.salesmagnet.referencedataservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ReferencedataserviceApplication {
	
	@RequestMapping("referencedata/hello")
    String hello() {
        return "Hello referencedata!!";
    }

	public static void main(String[] args) {
		SpringApplication.run(ReferencedataserviceApplication.class, args);
	}
}
