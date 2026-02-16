package com.githubsearcher.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.githubsearcher.dto.*;
import com.githubsearcher.entity.GithubRepoEntity;
import com.githubsearcher.repository.GithubRepository;
import com.githubsearcher.service.impl.GithubServiceImpl;

@ExtendWith(MockitoExtension.class)
class GithubServiceTest {

    @Mock
    private GithubRepository githubRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GithubServiceImpl githubService;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(githubService,
                "githubApiUrl",
                "https://api.github.com/search/repositories");
    }

    @Test
    void shouldInsertNewRepository() {

        GithubSearchRequest request = new GithubSearchRequest();
        request.setQuery("spring");

        GithubRepoItem item = new GithubRepoItem();
        item.setId(1L);
        item.setName("demo");

        GithubRepoItem.Owner owner = new GithubRepoItem.Owner();
        owner.setLogin("test");
        item.setOwner(owner);

        GithubApiResponse apiResponse = new GithubApiResponse();
        apiResponse.setItems(List.of(item));

        when(restTemplate.getForObject(anyString(), eq(GithubApiResponse.class)))
                .thenReturn(apiResponse);

        when(githubRepository.findByGithubRepoId(1L))
                .thenReturn(Optional.empty());

        when(githubRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        List<GithubRepoResponse> result =
                githubService.searchAndSaveRepositories(request);

        assertEquals(1, result.size());
        verify(githubRepository, times(1)).save(any());
    }

    @Test
    void shouldUpdateExistingRepository() {

        GithubSearchRequest request = new GithubSearchRequest();
        request.setQuery("spring");

        GithubRepoItem item = new GithubRepoItem();
        item.setId(1L);
        item.setName("updated");

        GithubRepoItem.Owner owner = new GithubRepoItem.Owner();
        owner.setLogin("test");
        item.setOwner(owner);

        GithubApiResponse apiResponse = new GithubApiResponse();
        apiResponse.setItems(List.of(item));

        GithubRepoEntity existing = new GithubRepoEntity();
        existing.setGithubRepoId(1L);
        existing.setName("old");

        when(restTemplate.getForObject(anyString(), eq(GithubApiResponse.class)))
                .thenReturn(apiResponse);

        when(githubRepository.findByGithubRepoId(1L))
                .thenReturn(Optional.of(existing));

        when(githubRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        githubService.searchAndSaveRepositories(request);

        assertEquals("updated", existing.getName());
    }
}
