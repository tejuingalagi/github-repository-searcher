package com.freightfox.githubsearcher.controller;

import com.freightfox.githubsearcher.dto.GithubRepoResponse;
import com.freightfox.githubsearcher.dto.GithubSearchRequest;
import com.freightfox.githubsearcher.dto.GithubSearchResponse;
import com.freightfox.githubsearcher.entity.GithubRepoEntity;
import com.freightfox.githubsearcher.repository.GithubRepository;
import com.freightfox.githubsearcher.service.GithubService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Sort;


import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/github")
public class GithubController {

    private final GithubService githubService;
    private final GithubRepository githubRepository;

    public GithubController(GithubService githubService,
                            GithubRepository githubRepository) {
        this.githubService = githubService;
        this.githubRepository = githubRepository;
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
    public ResponseEntity<List<GithubRepoResponse>> getRepositories(
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Integer minStars,
            @RequestParam(defaultValue = "stars") String sort) {

        Sort sorting;

        switch (sort.toLowerCase()) {
            case "forks":
                sorting = Sort.by(Sort.Direction.DESC, "forks");
                break;
            case "updated":
                sorting = Sort.by(Sort.Direction.DESC, "lastUpdated");
                break;
            default:
                sorting = Sort.by(Sort.Direction.DESC, "stars");
        }

        List<GithubRepoEntity> entities =
                githubRepository.findAll(sorting);

        // Apply filters manually
        if (language != null) {
            entities = entities.stream()
                    .filter(e -> language.equalsIgnoreCase(e.getLanguage()))
                    .toList();
        }

        if (minStars != null) {
            entities = entities.stream()
                    .filter(e -> e.getStars() != null && e.getStars() >= minStars)
                    .toList();
        }

        List<GithubRepoResponse> response =
                entities.stream()
                        .map(e -> new GithubRepoResponse(
                                e.getId(),
                                e.getGithubRepoId(),
                                e.getName(),
                                e.getDescription(),
                                e.getOwner(),
                                e.getLanguage(),
                                e.getStars(),
                                e.getForks(),
                                e.getLastUpdated()
                        ))
                        .toList();

        return ResponseEntity.ok(response);
    }

}

