package com.davidov.marvel;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ComponentScan
@Configuration
@ConfigurationProperties(prefix = "api.marvel")
@OpenAPIDefinition(info=@Info(
    title="Marvel API Microservice", 
    description = "A microservice BFF for the Marvel public API.<br/><br/>" + 
        "<b>Note:</b> Requires a valid Marvel API key - more info at the " +
        "<a href='https://developer.marvel.com' target='_blank'>Marvel Developer Website</a>.", 
    version = "v1.0.0"),
    servers = {
        @Server(
                description = "localhost",
                url = "http://localhost:8080"
        ),
        @Server(
                description = "SSL test server",
                url = "https://<ssl-test-server>"
        )
}
)
public class AppConfiguration {

    private String apiKey;
	private String privateKey;
}
