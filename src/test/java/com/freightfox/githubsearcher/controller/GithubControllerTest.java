package com.freightfox.githubsearcher.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.freightfox.githubsearcher.dto.GithubRepoResponse;
import com.freightfox.githubsearcher.repository.GithubRepository;
import com.freightfox.githubsearcher.service.GithubService;

@WebMvcTest(GithubController.class)
@AutoConfigureMockMvc
class GithubControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GithubService githubService;

    @MockBean
    private GithubRepository githubRepository; // ⭐ ADD THIS

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
                .andExpect(jsonPath("$.message").value("Repositories fetched and saved successfully"))
                .andExpect(jsonPath("$.repositories").isArray());
    }
}
