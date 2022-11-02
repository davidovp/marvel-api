package com.test.marvel;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class MarvelClientImpl implements MarvelClient {

    protected static final String MARVEL_URL_API = "https://gateway.marvel.com:443/v1/public";
    protected static final String MARVEL_URI_COMICS = "/comics";
    protected static final String MARVEL_URI_CHARACTERS = "/characters";
    protected static final String MARVEL_URI_CREATORS = "/creators";
    protected static final String MARVEL_URI_SERIES = "/series";
    protected static final String MARVEL_PARAM_TIMESTAMP = "ts";
    protected static final String MARVEL_PARAM_APIKEY = "apikey";
    protected static final String MARVEL_PARAM_HASH = "hash";
    protected static final String MARVEL_PARAM_LIMIT = "limit";

    protected static final int PAGE_SIZE = 5;

    @Autowired
    private MarvelApiProperties apiProps;

    private WebClient webClient;
    private ObjectMapper mapper;

    public MarvelClientImpl(WebClient.Builder webClientBuilder) {
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.webClient = webClientBuilder.baseUrl(MARVEL_URL_API)
            .build();
    }

    private ResponseSpec setupApiCall(WebClient webClient, String apiUri, MultiValueMap<String,String> params) {
        log.debug(">>> setupApiCall");

        log.info("Preparing Marvel API request");
        long timestamp = MarvelUtil.getTimestamp();
        ResponseSpec spec = webClient.get()
            .uri(uriBuilder -> uriBuilder.path(apiUri)
                .queryParam(MARVEL_PARAM_TIMESTAMP,  timestamp)
                .queryParam(MARVEL_PARAM_APIKEY, apiProps.getApiKey())
                .queryParam(MARVEL_PARAM_HASH, MarvelUtil.getHash(apiProps.getApiKey(), apiProps.getPrivateKey(), timestamp))
                .queryParam(MARVEL_PARAM_LIMIT, PAGE_SIZE)
                .queryParams(params)
	            .build())
            .header(HttpHeaders.ACCEPT_ENCODING, "gzip")
            .retrieve()
            .onStatus(HttpStatus::isError, response -> response.bodyToMono(String.class)
                .flatMap(error -> Mono.error(new RuntimeException(error))));

        return spec;
    }

    protected void logSuccess(String response) {
        if (log.isDebugEnabled()) {
            try {
                log.info("Marvel API call succeeded");
                log.debug("Payload: " + System.lineSeparator() +  mapper.readTree(response).toPrettyString());
            } catch (Exception e) {
                // do nothing for now
            }
        }
    }

    protected void logError(Throwable t) {
        log.error("Marvel API call error: ", t);
    }

    @Override
    public Mono<String> getComics(Optional<List<String>> characterIdList,
            Optional<List<String>> creatorIdList, 
            Optional<List<String>> seriesIdList) {
        log.debug(">>> getComics");
        
        log.info("Getting comics");
        log.info("Preparing Marvel query param filters");
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        characterIdList.ifPresent(list -> queryParams.addAll("characters", list));
        creatorIdList.ifPresent(list -> queryParams.addAll("creators", list));
        seriesIdList.ifPresent(list -> queryParams.addAll("series", list));
        log.info("Filters: {}", queryParams);

        ResponseSpec spec = setupApiCall(this.webClient, MARVEL_URI_COMICS, queryParams);
        log.info("Calling Marvel API service");
        Mono<String> result = spec.bodyToMono(String.class)
            .doOnSuccess(this::logSuccess)
            .doOnError(this::logError)
            .onErrorResume( e -> Mono.just(e.getMessage()));
        log.info("Service call finished");

        return result;
    }

	@Override
	public Mono<String> getComicsByCharacter(String characterId) {
        log.debug(">>> getComicsByCharacter");

        ResponseSpec spec = setupApiCall(this.webClient, 
            MARVEL_URI_CHARACTERS + "/" + characterId + MARVEL_URI_COMICS,
            null);

        Mono<String> result = spec.bodyToMono(String.class)
            .doOnSuccess(this::logSuccess)
            .doOnError(this::logError)
            .onErrorResume( e -> Mono.just(e.getMessage()));

        return result;
	}

	@Override
	public Mono<String> getComicsByCreator(String creatorId) {
        log.debug(">>> getComicsByCreator");

        ResponseSpec spec = setupApiCall(this.webClient, 
            MARVEL_URI_CREATORS + "/" + creatorId + MARVEL_URI_COMICS,
            null);

        Mono<String> result = spec.bodyToMono(String.class)
        .doOnSuccess(this::logSuccess)
        .doOnError(this::logError)
        .onErrorResume( e -> Mono.just(e.getMessage()));

        return result;
	}

	@Override
	public Mono<String> getComicsBySeries(String seriesId) {
        log.debug(">>> getComicsBySeries");

        ResponseSpec spec = setupApiCall(this.webClient, 
            MARVEL_URI_SERIES + "/" + seriesId + MARVEL_URI_COMICS,
            null);

        Mono<String> result = spec.bodyToMono(String.class)
            .doOnSuccess(this::logSuccess)
            .doOnError(this::logError)
            .onErrorResume( e -> Mono.just(e.getMessage()));

        return result;
	}

}
