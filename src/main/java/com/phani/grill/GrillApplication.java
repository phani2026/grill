package com.phani.grill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GrillApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrillApplication.class, args);
	}

}
