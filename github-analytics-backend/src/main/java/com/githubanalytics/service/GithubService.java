package com.githubanalytics.service;

import com.githubanalytics.client.GithubApiClient;
import com.githubanalytics.dto.RepoDTO;
import com.githubanalytics.dto.UserDTO;
import com.githubanalytics.mapper.GithubMapper;
import com.githubanalytics.repository.RepoRepository;
import com.githubanalytics.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GithubService {

    private final GithubApiClient githubApiClient;
    private final UserRepository userRepository;
    private final RepoRepository repoRepository;

    public UserDTO getUser(String username) {
        UserDTO user = githubApiClient.getUser(username);
        userRepository.save(GithubMapper.toEntity(user));
        return user;
    }

    @Transactional
    public List<RepoDTO> getRepos(String username) {
        List<RepoDTO> repos = githubApiClient.getRepos(username);

        repoRepository.deleteByOwnerLogin(username);
        repoRepository.saveAll(
                repos.stream()
                        .map(r -> GithubMapper.toEntity(username, r))
                        .toList());

        return repos;
    }

    public Map<String, Object> getStats(String username) {
        List<RepoDTO> repos = githubApiClient.getRepos(username);

        int totalStars = repos.stream()
                .mapToInt(RepoDTO::getStars)
                .sum();

        Map<String, Long> languages = new HashMap<>();
        repos.stream()
                .filter(r -> r.getLanguage() != null && !r.isFork())
                .forEach(r -> languages.merge(r.getLanguage(), 1L, Long::sum));

        RepoDTO topRepo = repos.stream()
                .filter(r -> !r.isFork())
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