package com.tinhnv.webflux;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;

@SpringBootApplication
public class SpringWebFluxApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringWebFluxApplication.class, args);
	}

	@Bean
	CommandLineRunner run() {
		return args -> System.out.println("webflux tutorial project!");
	}

}
