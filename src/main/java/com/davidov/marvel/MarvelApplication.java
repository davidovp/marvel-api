package com.davidov.marvel;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = {"com.davidov.marvel"})
public class MarvelApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(MarvelApplication.class)
			.web(WebApplicationType.REACTIVE)
			.run(args);
	}
}
