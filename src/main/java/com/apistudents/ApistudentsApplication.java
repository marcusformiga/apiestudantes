package com.apistudents;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableCaching
public class ApistudentsApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(ApistudentsApplication.class, args);
		//System.out.println(new BCryptPasswordEncoder().encode("teste"));
	
	}

}
