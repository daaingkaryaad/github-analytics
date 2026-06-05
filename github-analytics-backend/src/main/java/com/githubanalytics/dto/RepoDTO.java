package com.githubanalytics.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepoDTO {
    private Long id;
    private String name;
    @JsonProperty("full_name")
    private String fullName;
    private String description;
    private String language;
    @JsonProperty("stargazers_count")
    private int stars;
    @JsonProperty("forks_count")
    private int forks;
    @JsonProperty("html_url")
    private String htmlUrl;
    @JsonProperty("updated_at")
    private String updatedAt;
    private boolean fork;
}