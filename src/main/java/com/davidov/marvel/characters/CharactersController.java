package com.davidov.marvel.characters;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1")
@Tag(name="Characters", description = "Marvel characters related API calls")
public class CharactersController {

    @Autowired
    CharactersService characterService;

    @Operation(
        summary = "Get list of Marvel characters",
        description = "Get a list of Marvel characters, optionally filtered by lists of comics, creators or series id's")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Call successful"),
    @ApiResponse(responseCode = "409", description = "Marvel API server error"),
    @ApiResponse(responseCode = "500", description = "Server error")})
    @GetMapping(path = "/characters", produces = {"application/json"})
    public Mono<String> getCharacersWithFilters(
        @Parameter(description = "List of Marvel comics id's") @RequestParam("comics_ids") Optional<List<String>> comicsIdList,
        @Parameter(description = "List of Marvel creator id's") @RequestParam("creator_ids") Optional<List<String>> creatorIdList,
        @Parameter(description = "List of Marvel series id's") @RequestParam("series_ids") Optional<List<String>>  seriesIdList) {
        
        log.debug(">>> getCharacersWithFilters");
        
        if (log.isInfoEnabled()) {
            log.info("Param 'comicsIds': {}", comicsIdList);
            log.info("Param 'creatorIds': {}", creatorIdList);
            log.info("Param 'seriesIds': {}", seriesIdList);
        }

        return characterService.getCharacters(comicsIdList, creatorIdList, seriesIdList);
    }

    @Operation(
        summary = "Search for Marvel characters",
        description = "Get a list of Marvel characters matching search criteria")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Call successful"),
    @ApiResponse(responseCode = "409", description = "Marvel API server error"),
    @ApiResponse(responseCode = "500", description = "Server error")})
    @GetMapping(path = "/characters/search", produces = {"application/json"})
    public Mono<String> getCharacersSearch(
        @Parameter(description = "Name of character to search") @RequestParam("name") Optional<String> characterName) {
        
        log.debug(">>> getCharacersSearch");
        log.info("Param 'name': {}", characterName);

        return characterService.getCharactersByName(characterName);
    }
}