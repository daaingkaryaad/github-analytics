package com.githubanalytics.controller;

import com.githubanalytics.dto.UserDTO;
import com.githubanalytics.exception.GithubUserNotFoundException;
import com.githubanalytics.service.GithubService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GithubController.class)
class GithubControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GithubService service;

    @Test
    void getUser_returns200WithProfile() throws Exception {
        when(service.getUser("octocat")).thenReturn(
                UserDTO.builder().login("octocat").name("The Octocat").followers(100).build());

        mockMvc.perform(get("/api/users/octocat"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value("octocat"))
                .andExpect(jsonPath("$.followers").value(100));
    }

    @Test
    void getUser_unknownUsername_returns404FromExceptionHandler() throws Exception {
        when(service.getUser("ghost")).thenThrow(new GithubUserNotFoundException("ghost"));

        mockMvc.perform(get("/api/users/ghost"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value(Matchers.containsString("ghost")));
    }
}
