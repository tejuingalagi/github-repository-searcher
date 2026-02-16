package com.githubsearcher.dto;

import javax.validation.constraints.NotBlank;

public class GithubSearchRequest {

    @NotBlank
    private String query;

    private String language;

    private String sort = "stars"; // default

    // getters & setters
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
