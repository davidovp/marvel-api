package com.davidov.marvel.comics;

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
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1")
public class ComicsController {

    @Autowired
    ComicsService comicsService;

    @Operation(
        summary = "Get list of Marvel comics",
        description = "Get a list of Marvel comics, optionally filtered by lists of characters, creators or series id's")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Call successful"),
    @ApiResponse(responseCode = "409", description = "Marvel API server error"),
    @ApiResponse(responseCode = "500", description = "Server error")})
    @GetMapping(path = "/comics", produces = {"application/json"})
    public Mono<String> getComicsWithFilters(
        @Parameter(description = "List of Marvel character id's") @RequestParam("character_ids") Optional<List<String>> characterIdList,
        @Parameter(description = "List of Marvel creator id's") @RequestParam("creator_ids") Optional<List<String>> creatorIdList,
        @Parameter(description = "List of Marvel series id's") @RequestParam("series_ids") Optional<List<String>>  seriesIdList) {
        
        log.debug(">>> getComicsWithFilters");
        
        if (log.isInfoEnabled()) {
            log.info("Param 'characterIds': {}", characterIdList);
            log.info("Param 'creatorIds': {}", creatorIdList);
            log.info("Param 'seriesIds': {}", seriesIdList);
        }

        return comicsService.getComics(characterIdList, creatorIdList, seriesIdList);
    }

}