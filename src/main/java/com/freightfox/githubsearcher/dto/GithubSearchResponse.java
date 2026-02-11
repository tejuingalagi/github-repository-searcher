package com.freightfox.githubsearcher.dto;

import java.util.List;

public class GithubSearchResponse {

    private String message;
    private List<GithubRepoResponse> repositories;

    public GithubSearchResponse(String message, List<GithubRepoResponse> repositories) {
        this.message = message;
        this.repositories = repositories;
    }

    public String getMessage() {
        return message;
    }

    public List<GithubRepoResponse> getRepositories() {
        return repositories;
    }
}
