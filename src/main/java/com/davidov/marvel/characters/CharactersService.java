package com.davidov.marvel.characters;

import java.util.List;
import java.util.Optional;

import reactor.core.publisher.Mono;

public interface CharactersService {
    
    public Mono<String> getCharacters(
        Optional<List<String>> comicsIdList, 
        Optional<List<String>> creatorIdList,
        Optional<List<String>> seriesIdList);

    public Mono<String> getCharactersByComic(String comicId);

    public Mono<String> getCharactersByCreator(String creatorId);

    public Mono<String> getCharactersByEvent(String eventId);
    
    public Mono<String> getCharactersBySeries(String seriesId);
}
