package com.freightfox.githubsearcher.dto;

import java.util.List;

public class GithubApiResponse {

    private List<GithubRepoItem> items;

    public List<GithubRepoItem> getItems() {
        return items;
    }

    public void setItems(List<GithubRepoItem> items) {
        this.items = items;
    }
}
