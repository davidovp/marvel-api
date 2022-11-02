package com.test.marvel;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = { "com.test.marvel"})
public class MarvelApiApp {

	public static void main(String[] args) {
		new SpringApplicationBuilder(MarvelApiApp.class)
			.web(WebApplicationType.REACTIVE)
			.run(args);
	}
}
