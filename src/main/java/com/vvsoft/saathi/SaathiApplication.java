package com.vvsoft.saathi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class SaathiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SaathiApplication.class, args);
	}

	@Bean
	public CommandLineRunner applicationStarted() {
		return args -> log.info("Welcome Friend");
	}

}
