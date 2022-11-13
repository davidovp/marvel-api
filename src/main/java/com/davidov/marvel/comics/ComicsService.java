package com.davidov.marvel.comics;

import java.util.List;
import java.util.Optional;

import reactor.core.publisher.Mono;

public interface ComicsService {
    public Mono<String> getComics(Optional<List<String>> creatorIdList,
            Optional<List<String>> characterIdList, Optional<List<String>> seriesIdList);

    public Mono<String> getComicsByTitle(Optional<String> comicTitle);

    public Mono<String> getComicsByCharacter(String characterId);

    public Mono<String> getComicsByCreator(String creatorId);

    public Mono<String> getComicsByEvent(String eventId);
    
    public Mono<String> getComicsBySeries(String seriesId);
}
