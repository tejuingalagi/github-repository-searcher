package com.githubsearcher.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.githubsearcher.dto.GithubRepoResponse;
import com.githubsearcher.dto.GithubSearchRequest;
import com.githubsearcher.dto.GithubSearchResponse;
import com.githubsearcher.dto.PagedResponse;
import com.githubsearcher.entity.GithubRepoEntity;
import com.githubsearcher.repository.GithubRepository;
import com.githubsearcher.service.GithubService;

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
    public ResponseEntity<PagedResponse<GithubRepoResponse>> getRepositories(
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Integer minStars,
            @RequestParam(defaultValue = "stars") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<GithubRepoResponse> result =
                githubService.getStoredRepositories(language, minStars, sort, page, size);

        PagedResponse<GithubRepoResponse> response =
                new PagedResponse<>(
                        result.getContent(),
                        result.getNumber(),
                        result.getSize(),
                        result.getTotalElements()
                );

        return ResponseEntity.ok(response);
    }



}

