package com.davidov.marvel.characters;

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

import com.davidov.marvel.AppConfiguration;
import com.davidov.marvel.MarvelUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class CharactersServiceImpl implements CharactersService {

    @Autowired
    private AppConfiguration config;

    private WebClient webClient;
    private ObjectMapper mapper;

    public CharactersServiceImpl(WebClient.Builder webClientBuilder) {
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.webClient = webClientBuilder.baseUrl(MarvelUtil.MARVEL_URL_API)
            .build();
    }

    private ResponseSpec setupApiCall(WebClient webClient, String apiUri, MultiValueMap<String,String> params) {
        log.debug(">>> setupApiCall");

        log.info("Preparing Marvel API request");
        long timestamp = MarvelUtil.getTimestamp();
        ResponseSpec spec = webClient.get()
            .uri(uriBuilder -> uriBuilder.path(apiUri)
                .queryParam(MarvelUtil.MARVEL_PARAM_TIMESTAMP,  timestamp)
                .queryParam(MarvelUtil.MARVEL_PARAM_APIKEY, config.getApiKey())
                .queryParam(MarvelUtil.MARVEL_PARAM_HASH, MarvelUtil.getHash(config.getApiKey(), config.getPrivateKey(), timestamp))
                .queryParam(MarvelUtil.MARVEL_PARAM_LIMIT, MarvelUtil.DEFAULT_PAGE_SIZE)
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
    public Mono<String> getCharacters(Optional<List<String>> comicsIdList,
            Optional<List<String>> creatorIdList, 
            Optional<List<String>> seriesIdList) {
        log.debug(">>> getCharacters");
        
        log.info("Getting characters");
        log.info("Preparing Marvel query param filters");
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        comicsIdList.ifPresent(list -> queryParams.addAll("comics", list));
        creatorIdList.ifPresent(list -> queryParams.addAll("creators", list));
        seriesIdList.ifPresent(list -> queryParams.addAll("series", list));
        log.info("Filters: {}", queryParams);

        ResponseSpec spec = setupApiCall(this.webClient, MarvelUtil.MARVEL_URI_CHARACTERS, queryParams);
        log.info("Calling Marvel API service");
        Mono<String> result = spec.bodyToMono(String.class)
            .doOnSuccess(this::logSuccess)
            .doOnError(this::logError)
            .onErrorResume( e -> Mono.just(e.getMessage()));
        log.info("Service call finished");

        return result;
    }

    @Override
    public Mono<String> getCharactersByName(Optional<String> characterName) {
        log.debug(">>> getCharactersByName");
        
        log.info("Getting characters by name");
        log.info("Preparing Marvel query param filters");
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        characterName.ifPresent(name -> queryParams.add("nameStartsWith", name));
        log.info("Filters: {}", queryParams);

        ResponseSpec spec = setupApiCall(this.webClient, MarvelUtil.MARVEL_URI_CHARACTERS, queryParams);
        log.info("Calling Marvel API service");
        Mono<String> result = spec.bodyToMono(String.class)
            .doOnSuccess(this::logSuccess)
            .doOnError(this::logError)
            .onErrorResume( e -> Mono.just(e.getMessage()));
        log.info("Service call finished");

        return result;
    }

	@Override
	public Mono<String> getCharactersByComic(String comicId) {
        log.debug(">>> getCharactersByComic");

        ResponseSpec spec = setupApiCall(this.webClient, 
            MarvelUtil.MARVEL_URI_COMICS + "/" + comicId + MarvelUtil.MARVEL_URI_CHARACTERS,
            null);

        Mono<String> result = spec.bodyToMono(String.class)
            .doOnSuccess(this::logSuccess)
            .doOnError(this::logError)
            .onErrorResume( e -> Mono.just(e.getMessage()));

        return result;
	}

	@Override
	public Mono<String> getCharactersByCreator(String creatorId) {
        log.debug(">>> getCharactersByCreator");

        ResponseSpec spec = setupApiCall(this.webClient, 
            MarvelUtil.MARVEL_URI_CREATORS + "/" + creatorId + MarvelUtil.MARVEL_URI_CHARACTERS,
            null);

        Mono<String> result = spec.bodyToMono(String.class)
        .doOnSuccess(this::logSuccess)
        .doOnError(this::logError)
        .onErrorResume( e -> Mono.just(e.getMessage()));

        return result;
	}

	@Override
	public Mono<String> getCharactersBySeries(String seriesId) {
        log.debug(">>> getCharactersBySeries");

        ResponseSpec spec = setupApiCall(this.webClient, 
            MarvelUtil.MARVEL_URI_SERIES + "/" + seriesId + MarvelUtil.MARVEL_URI_CHARACTERS,
            null);

        Mono<String> result = spec.bodyToMono(String.class)
            .doOnSuccess(this::logSuccess)
            .doOnError(this::logError)
            .onErrorResume( e -> Mono.just(e.getMessage()));

        return result;
	}

    @Override
    public Mono<String> getCharactersByEvent(String eventId) {
        log.debug(">>> getCharactersByEvent");

        ResponseSpec spec = setupApiCall(this.webClient, 
            MarvelUtil.MARVEL_URI_EVENTS + "/" + eventId + MarvelUtil.MARVEL_URI_CHARACTERS,
            null);

        Mono<String> result = spec.bodyToMono(String.class)
            .doOnSuccess(this::logSuccess)
            .doOnError(this::logError)
            .onErrorResume( e -> Mono.just(e.getMessage()));

        return result;
    }


}
