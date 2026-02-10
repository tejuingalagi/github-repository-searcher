package com.freightfox.githubsearcher.dto;

import java.time.LocalDateTime;

public class GithubRepoResponse {

    private Long id;
    private Long githubRepoId;
    private String name;
    private String description;
    private String owner;
    private String language;
    private Integer stars;
    private Integer forks;
    private LocalDateTime lastUpdated;

    public GithubRepoResponse() {}

    public GithubRepoResponse(Long id,
                              Long githubRepoId,
                              String name,
                              String description,
                              String owner,
                              String language,
                              Integer stars,
                              Integer forks,
                              LocalDateTime lastUpdated) {
        this.id = id;
        this.githubRepoId = githubRepoId;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.language = language;
        this.stars = stars;
        this.forks = forks;
        this.lastUpdated = lastUpdated;
    }

    public Long getId() {
        return id;
    }

    public Long getGithubRepoId() {
        return githubRepoId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getOwner() {
        return owner;
    }

    public String getLanguage() {
        return language;
    }

    public Integer getStars() {
        return stars;
    }

    public Integer getForks() {
        return forks;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
}
