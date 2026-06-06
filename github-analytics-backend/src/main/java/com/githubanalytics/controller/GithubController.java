package com.githubanalytics.controller;

import com.githubanalytics.dto.CommitDTO;
import com.githubanalytics.dto.RepoDTO;
import com.githubanalytics.dto.UserDTO;
import com.githubanalytics.service.GithubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GithubController {

    private final GithubService githubService;

    @GetMapping("/users/{username}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String username) {
        return ResponseEntity.ok(githubService.getUser(username));
    }

    @GetMapping("/users/{username}/repos")
    public ResponseEntity<List<RepoDTO>> getRepos(@PathVariable String username) {
        return ResponseEntity.ok(githubService.getRepos(username));
    }

    @GetMapping("/users/{username}/stats")
    public ResponseEntity<Map<String, Object>> getStats(@PathVariable String username) {
        return ResponseEntity.ok(githubService.getStats(username));
    }
    @GetMapping("/users/{username}/repos/{repo}/commits")
    public ResponseEntity<List<CommitDTO>> getCommits(
        @PathVariable String username,
        @PathVariable String repo) {
            
            return ResponseEntity.ok(
                githubService.getCommits(username, repo)
            );
        }
}