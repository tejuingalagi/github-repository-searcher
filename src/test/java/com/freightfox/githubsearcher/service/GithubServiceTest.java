package com.freightfox.githubsearcher.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import com.freightfox.githubsearcher.dto.GithubApiResponse;
import com.freightfox.githubsearcher.dto.GithubRepoItem;
import com.freightfox.githubsearcher.dto.GithubRepoResponse;
import com.freightfox.githubsearcher.dto.GithubSearchRequest;
import com.freightfox.githubsearcher.repository.GithubRepository;
import com.freightfox.githubsearcher.service.impl.GithubServiceImpl;

@ExtendWith(MockitoExtension.class)
class GithubServiceTest {

    @Mock
    private GithubRepository githubRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GithubServiceImpl githubService;

    @Test
    void shouldFetchAndSaveRepositories() {

        GithubSearchRequest request = new GithubSearchRequest();
        request.setQuery("spring boot");
        request.setLanguage("Java");
        request.setSort("stars");

        GithubRepoItem item = new GithubRepoItem();
        item.setId(1L);
        item.setName("spring-boot-demo");
        item.setLanguage("Java");

        GithubRepoItem.Owner owner = new GithubRepoItem.Owner();
        owner.setLogin("spring");
        item.setOwner(owner);

        GithubApiResponse apiResponse = new GithubApiResponse();
        apiResponse.setItems(List.of(item));

        when(restTemplate.getForObject(anyString(), eq(GithubApiResponse.class)))
                .thenReturn(apiResponse);

        when(githubRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        List<GithubRepoResponse> result =
                githubService.searchAndSaveRepositories(request);

        assertEquals(1, result.size());
        assertEquals("spring-boot-demo", result.get(0).getName());
    }
}
