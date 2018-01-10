package com.nsb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ComponentScan(basePackages = {"com.nsb.*", "com.nsb.config.*"})
public class NsbApplication {

	public static void main(String[] args) {
		SpringApplication.run(NsbApplication.class, args);
	}
}
