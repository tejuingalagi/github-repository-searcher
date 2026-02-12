package com.freightfox.githubsearcher.service.impl;

import com.freightfox.githubsearcher.dto.GithubApiResponse;
import com.freightfox.githubsearcher.dto.GithubRepoItem;
import com.freightfox.githubsearcher.dto.GithubRepoResponse;
import com.freightfox.githubsearcher.dto.GithubSearchRequest;
import com.freightfox.githubsearcher.entity.GithubRepoEntity;
import com.freightfox.githubsearcher.exception.GithubApiException;
import com.freightfox.githubsearcher.repository.GithubRepository;
import com.freightfox.githubsearcher.service.GithubService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GithubServiceImpl implements GithubService {

    private final RestTemplate restTemplate;
    private final GithubRepository githubRepository;

    @Value("${github.api.url}")
    private String githubApiUrl;

    public GithubServiceImpl(RestTemplate restTemplate,
                             GithubRepository githubRepository) {
        this.restTemplate = restTemplate;
        this.githubRepository = githubRepository;
    }

    @Override
    public List<GithubRepoResponse> searchAndSaveRepositories(GithubSearchRequest request) {

        try {
            StringBuilder query = new StringBuilder();
            query.append("?q=").append(request.getQuery());

            if (request.getLanguage() != null && !request.getLanguage().isEmpty()) {
                query.append("+language:").append(request.getLanguage());
            }

            if (request.getSort() != null && !request.getSort().isEmpty()) {
                query.append("&sort=").append(request.getSort());
            }

            String url = githubApiUrl + query;

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
                entity.setOwner(item.getOwner().getLogin());
                entity.setLanguage(item.getLanguage());
                entity.setStars(item.getStars());
                entity.setForks(item.getForks());
                entity.setLastUpdated(item.getLastUpdated());

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

        } catch (Exception ex) {
            throw new GithubApiException("Failed to fetch repositories from GitHub", ex);
        }
    }


}
