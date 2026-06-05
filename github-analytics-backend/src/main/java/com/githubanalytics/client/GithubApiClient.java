package com.githubanalytics.client;

import com.githubanalytics.dto.RepoDTO;
import com.githubanalytics.dto.UserDTO;
import com.githubanalytics.exception.GithubUserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GithubApiClient {

    private final WebClient webClient;

    @Value("${github.api.token:}")
    private String token;

    @Cacheable(value = "githubUser", key = "#username")
    public UserDTO getUser(String username) {
        return webClient.get()
                .uri("/users/{username}", username)
                .headers(h -> { if (!token.isBlank()) h.setBearerAuth(token); })
                .retrieve()
                .onStatus(status -> status.value() == 404,
                        r -> Mono.error(new GithubUserNotFoundException(username)))
                .bodyToMono(UserDTO.class)
                .block();
    }

    @Cacheable(value = "githubRepos", key = "#username")
    public List<RepoDTO> getRepos(String username) {
        return webClient.get()
                .uri("/users/{username}/repos?per_page=100&sort=updated", username)
                .headers(h -> { if (!token.isBlank()) h.setBearerAuth(token); })
                .retrieve()
                .onStatus(status -> status.value() == 404,
                        r -> Mono.error(new GithubUserNotFoundException(username)))
                .bodyToFlux(RepoDTO.class)
                .collectList()
                .block();
    }
}