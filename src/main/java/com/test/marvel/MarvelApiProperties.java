package com.test.marvel;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EnableWebFlux
@ComponentScan
@Configuration
@ConfigurationProperties(prefix = "api.marvel")
public class MarvelApiProperties {

    private String apiKey;
	private String privateKey;
}
