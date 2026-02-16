package com.githubsearcher.service.impl;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.githubsearcher.dto.GithubApiResponse;
import com.githubsearcher.dto.GithubRepoItem;
import com.githubsearcher.dto.GithubRepoResponse;
import com.githubsearcher.dto.GithubSearchRequest;
import com.githubsearcher.entity.GithubRepoEntity;
import com.githubsearcher.exception.GithubApiException;
import com.githubsearcher.repository.GithubRepository;
import com.githubsearcher.service.GithubService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Service
public class GithubServiceImpl implements GithubService {

    private final RestTemplate restTemplate;
    private final GithubRepository githubRepository;

    @Value("${github.api.url}")
    private String githubApiUrl;
    
    private static final List<String> ALLOWED_SORT =
            List.of("stars","forks","updated");


    public GithubServiceImpl(RestTemplate restTemplate,
                             GithubRepository githubRepository) {
        this.restTemplate = restTemplate;
        this.githubRepository = githubRepository;
    }
    

   
    // SEARCH FROM GITHUB + SAVE
    @Override
    public List<GithubRepoResponse> searchAndSaveRepositories(GithubSearchRequest request) {

        try {

            // -------- SORT VALIDATION ----------
            if (request.getSort() != null &&
                    !ALLOWED_SORT.contains(request.getSort().toLowerCase())) {
                throw new IllegalArgumentException(
                        "Invalid sort field. Use stars, forks or updated");
            }

            StringBuilder query = new StringBuilder();
            query.append("?q=").append(URLEncoder.encode(request.getQuery(), StandardCharsets.UTF_8));

            if (request.getLanguage() != null && !request.getLanguage().isEmpty()) {
                query.append("+language:").append(request.getLanguage());
            }

            if (request.getSort() != null && !request.getSort().isEmpty()) {
                query.append("&sort=").append(request.getSort());
            }

            // pagination
            query.append("&per_page=50&page=1");

            String url = githubApiUrl + query.toString();

            GithubApiResponse response =
                    restTemplate.getForObject(url, GithubApiResponse.class);

            if (response == null || response.getItems() == null) {
                throw new GithubApiException("Empty response from GitHub API");
            }

            List<GithubRepoResponse> result = new ArrayList<>();

            for (GithubRepoItem item : response.getItems()) {

                GithubRepoEntity entity =
                        githubRepository.findByGithubRepoId(item.getId())
                                .orElse(new GithubRepoEntity());

                entity.setGithubRepoId(item.getId());
                entity.setName(item.getName());
                entity.setDescription(item.getDescription());

                String owner = (item.getOwner() != null && item.getOwner().getLogin() != null)
                        ? item.getOwner().getLogin()
                        : "unknown";
                entity.setOwner(owner);

                if (item.getLanguage() != null) entity.setLanguage(item.getLanguage());
                if (item.getStars() != null) entity.setStars(item.getStars());
                if (item.getForks() != null) entity.setForks(item.getForks());
                if (item.getLastUpdated() != null) entity.setLastUpdated(item.getLastUpdated());

                GithubRepoEntity saved = githubRepository.save(entity);

                result.add(new GithubRepoResponse(
                        saved.getId(),
                        saved.getGithubRepoId(),
                        saved.getName(),
                        saved.getDescription(),
                        saved.getOwner(),
                        saved.getLanguage(),
                        saved.getStars(),
                        saved.getForks(),
                        saved.getLastUpdated()
                ));
            }

            return result;

        } catch (HttpClientErrorException ex) {

            // -------- RATE LIMIT FIX ----------
            if (ex.getStatusCode().value() == 403) {
                throw new GithubApiException("GitHub API rate limit exceeded. Try again later.");
            }

            throw new GithubApiException("GitHub API error: " + ex.getStatusCode());

        } catch (IllegalArgumentException ex) {
            throw ex; // let GlobalExceptionHandler return 400
        } catch (Exception ex) {
            throw new GithubApiException("Failed to fetch repositories from GitHub", ex);
        }

    }


    
    // FETCH FROM DATABASE
    @Override
    public Page<GithubRepoResponse> getStoredRepositories(
            String language,
            Integer minStars,
            String sort,
            int page,
            int size) {

        if (sort == null) sort = "stars";
        sort = sort.toLowerCase();

        Sort sorting;

        switch (sort) {
            case "forks":
                sorting = Sort.by("forks").descending();
                break;
            case "updated":
                sorting = Sort.by("lastUpdated").descending();
                break;
            case "stars":
                sorting = Sort.by("stars").descending();
                break;
            default:
                throw new IllegalArgumentException("Invalid sort field. Use stars, forks or updated");
        }

        Pageable pageable = PageRequest.of(page, size, sorting);

        Page<GithubRepoEntity> entityPage =
                githubRepository.searchRepositories(language, minStars, pageable);

        return entityPage.map(e -> new GithubRepoResponse(
                e.getId(),
                e.getGithubRepoId(),
                e.getName(),
                e.getDescription(),
                e.getOwner(),
                e.getLanguage(),
                e.getStars(),
                e.getForks(),
                e.getLastUpdated()
        ));
    }
}
