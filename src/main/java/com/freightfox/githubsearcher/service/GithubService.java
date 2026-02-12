package com.freightfox.githubsearcher.service;

import java.util.List;

import com.freightfox.githubsearcher.dto.GithubRepoResponse;
import com.freightfox.githubsearcher.dto.GithubSearchRequest;

public interface GithubService {
    List<GithubRepoResponse> searchAndSaveRepositories(GithubSearchRequest request);
}

