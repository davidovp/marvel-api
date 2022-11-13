# marvel-api

## This API provides access to the [Marvel Comics Public API](https://developer.marvel.com/).

<br/>

### Start-up / Running
To run this Spring Boot app, first make sure to set the Marvel API/private key using the following properties in ```src/main/resources/application.properties```

1. ```api.marvel.apiKey``` to your Marvel API key
2. ```api.marvel.privateKey``` to your Marvel API private key

To run with Gradle, execute the applicable command from the top-level project directory to start the server*:
|OS|Command|
|:---:|:---|
|Linux/macOS|```./gradlew bootRun```|
|Windows|```gradlew.bat bootRun```|

To run with Docker:
1. Install Docker (https://docs.docker.com/get-docker/)
2. Run ```./gradlew bootBuildImage```|
3. Run ```docker run -p 8080:8080 -p 9090:9090 marvel-api:<version>```

<sup>* - Make sure you have a Java JRE/JDK installed and that the ```java```/```java.exe``` executable is in your system path.<sup>

Base service URL:  http://localhost:8080/api/v1/...

### API Documentation
- http://localhost:9090/actuator/openapi
- http://localhost:9090/actuator/swagger-ui

### Technologies
- Spring Boot 2.75
- Spring WebFlux Reactive Framework
- Spring Actuator
- Springdoc
- Lombok - reduce biolerplate code (getters/setters/logger/etc)
- Slf4j Logging (w/ Log4j) - implementation agnostic logging
- Gradle 7
- JDK 11

### Architecture/Design
- Simple REST API with single service call method which takes 0 to 3 filters (lists of characters, creators, series id's) (3 add'l exp methods)
- Utilizes WebFlux WebClient; returns String containing Marvel API JSON response as-is
- Limited page size to only 5 records/call (development setting)
- Limited error handler returns JSON error from Marvel API call

### Future improvements/enhancements
1. Add proper Marvel API error handling/responses
2. Add API security - OAuth2/OIDC
3. Add API caching & Etags handling to reduce b/w usage/latency
4. Add ELK/Splunk style logs & analysis
5. Add Observability metrics publishing (Datadog, Influx, etc.)
6. Add multi-environment support Spring Profiles/Env and/or Spring Cloud Config
7. Containerize app with Docker/OCI image/deploy
8. Add 'feature' management (Togglz/FF4J) - pre-deploying dormant API features, runtime feature enable/disable, a/b testing, etc.
