# marvel-api

## This API provides access to the [Marvel Comics Public API](https://developer.marvel.com/).

<br/>

### Start-up / Running
To run this Spring Boot app, first set the following 2 properties in /resources/application.properties:

1. Set **api.marvel.apiKey** to your Marvel API key
2. Set **api.marvel.privateKey** to your Marvel API private key

Next, if you have **Gradle** installed and in your system path, simply run 'gradle bootRun' from project directory.

Service URL:  http://localhost:8080/api/v1/comics

### API Documentation
- http://localhost:8080/v3/api-docs
- http://localhost:8080/swagger-ui/index.html

### Technologies
- Spring Boot 2.75
- Spring WebFlux Reactive Framework
- Lombok - reduces biolerplate code (getters/setters/loggers)
- Slf4j Logging (w/ Log4j) - implementation agnostic logging
- Gradle
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
7. Create Docker/OCI image to containerize & deploy app
8. Add 'feature' management (Togglz/FF4J) - pre-deploying dormant API features, runtime feature enable/disable, a/b testing, etc.