package com.githubanalytics.service;

import com.githubanalytics.client.GithubApiClient;
import com.githubanalytics.dto.RepoDTO;
import com.githubanalytics.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GithubService {

    private final GithubApiClient githubApiClient;

    public UserDTO getUser(String username) {
        return githubApiClient.getUser(username);
    }

    public List<RepoDTO> getRepos(String username) {
        return githubApiClient.getRepos(username);
    }

    public Map<String, Object> getStats(String username) {
        List<RepoDTO> repos = getRepos(username);

        int totalStars = repos.stream()
                .mapToInt(RepoDTO::getStars)
                .sum();

        Map<String, Long> languages = new HashMap<>();
        repos.stream()
                .filter(r -> r.getLanguage() != null && !r.isFork())
                .forEach(r -> languages.merge(r.getLanguage(), 1L, Long::sum));

        RepoDTO topRepo = repos.stream()
                .max((a, b) -> Integer.compare(a.getStars(), b.getStars()))
                .orElse(null);

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalStars", totalStars);
        stats.put("totalRepos", repos.size());
        stats.put("languages", languages);
        stats.put("topRepo", topRepo);
        return stats;
    }
}