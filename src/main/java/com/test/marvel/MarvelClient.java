package com.test.marvel;

import java.util.List;
import java.util.Optional;

import reactor.core.publisher.Mono;

public interface MarvelClient {
    public Mono<String> getComics(Optional<List<String>> creatorIdList,
            Optional<List<String>> characterIdList, Optional<List<String>> seriesIdList);

    public Mono<String> getComicsByCharacter(String characterId);

    public Mono<String> getComicsByCreator(String creatorId);

    public Mono<String> getComicsBySeries(String seriesId);
}
