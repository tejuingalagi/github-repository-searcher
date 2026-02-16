package com.githubsearcher.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.githubsearcher.dto.GithubRepoResponse;
import com.githubsearcher.dto.GithubSearchRequest;

public interface GithubService {

    List<GithubRepoResponse> searchAndSaveRepositories(GithubSearchRequest request);

    Page<GithubRepoResponse> getStoredRepositories(
            String language,
            Integer minStars,
            String sort,
            int page,
            int size
    );
}
