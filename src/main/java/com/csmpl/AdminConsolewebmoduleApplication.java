package com.csmpl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication//(scanBasePackages = {"com.csmpl"})
@EnableScheduling
public class AdminConsolewebmoduleApplication extends SpringBootServletInitializer{

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		
		return builder.sources(AdminConsolewebmoduleApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(AdminConsolewebmoduleApplication.class, args);
	}
}
