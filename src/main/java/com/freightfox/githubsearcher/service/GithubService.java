package com.freightfox.githubsearcher.service;

import com.freightfox.githubsearcher.dto.GithubSearchRequest;

public interface GithubService {
    void searchAndSaveRepositories(GithubSearchRequest request);
}
