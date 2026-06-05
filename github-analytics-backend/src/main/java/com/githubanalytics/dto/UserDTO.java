package com.githubanalytics.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private String login;
    private String name;
    private String bio;
    @JsonProperty("avatar_url")
    private String avatarUrl;
    @JsonProperty("html_url")
    private String htmlUrl;
    private int followers;
    private int following;
    @JsonProperty("public_repos")
    private int publicRepos;
    @JsonProperty("created_at")
    private String createdAt;
}