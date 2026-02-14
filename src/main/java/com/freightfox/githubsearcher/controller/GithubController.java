package com.freightfox.githubsearcher.controller;

import com.freightfox.githubsearcher.dto.GithubRepoResponse;
import com.freightfox.githubsearcher.dto.GithubSearchRequest;
import com.freightfox.githubsearcher.dto.GithubSearchResponse;
import com.freightfox.githubsearcher.entity.GithubRepoEntity;
import com.freightfox.githubsearcher.repository.GithubRepository;
import com.freightfox.githubsearcher.service.GithubService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;


import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/github")
public class GithubController {

    private final GithubService githubService;
    

    public GithubController(GithubService githubService) {
        this.githubService = githubService;
        
    }

    // POST - search and save
    @PostMapping("/search")
    public ResponseEntity<GithubSearchResponse> searchRepositories(
            @Valid @RequestBody GithubSearchRequest request) {

        List<GithubRepoResponse> repositories =
                githubService.searchAndSaveRepositories(request);

        GithubSearchResponse response =
                new GithubSearchResponse(
                        "Repositories fetched and saved successfully",
                        repositories
                );

        return ResponseEntity.ok(response);
    }


    // GET - retrieve stored
    @GetMapping("/repositories")
    public ResponseEntity<Page<GithubRepoResponse>> getRepositories(
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Integer minStars,
            @RequestParam(defaultValue = "stars") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                githubService.getStoredRepositories(language, minStars, sort, page, size)
        );
    }


}

