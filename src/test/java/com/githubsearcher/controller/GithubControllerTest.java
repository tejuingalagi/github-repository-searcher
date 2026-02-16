package com.githubsearcher.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.githubsearcher.dto.GithubRepoResponse;
import com.githubsearcher.service.GithubService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import static org.mockito.ArgumentMatchers.*;


@WebMvcTest(GithubController.class)
class GithubControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GithubService githubService;

    // SUCCESS CASE
    @Test
    void shouldSearchRepositories() throws Exception {

        when(githubService.searchAndSaveRepositories(any()))
                .thenReturn(List.of(new GithubRepoResponse(
                        1L,1L,"demo","desc","me","Java",10,2,null
                )));

        mockMvc.perform(post("/api/github/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"query\":\"spring boot\",\"language\":\"Java\",\"sort\":\"stars\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.repositories").isArray());
    }

    // VALIDATION TEST 
    @Test
    void shouldReturn400WhenQueryMissing() throws Exception {

        mockMvc.perform(post("/api/github/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"language\":\"Java\"}"))
                .andExpect(status().isBadRequest());
    }

    // GET TEST
    @Test
    void shouldReturnRepositories() throws Exception {

        // create fake page response
        List<GithubRepoResponse> list = List.of(
                new GithubRepoResponse(1L,1L,"demo","desc","me","Java",10,2,null)
        );

        Page<GithubRepoResponse> page =
                new PageImpl<>(list, PageRequest.of(0,10),1);

        when(githubService.getStoredRepositories(any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(page);

        mockMvc.perform(get("/api/github/repositories")
                .param("language","Java")
                .param("minStars","10")
                .param("sort","stars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

}
