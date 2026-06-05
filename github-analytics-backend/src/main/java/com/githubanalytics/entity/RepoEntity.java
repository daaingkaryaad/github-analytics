package com.githubanalytics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "github_repos", indexes = @Index(name = "idx_repo_owner", columnList = "ownerLogin"))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepoEntity {

    @Id
    private Long id;

    private String ownerLogin;

    private String name;
    private String fullName;

    @Column(length = 1000)
    private String description;

    private String language;
    private int stars;
    private int forks;
    private String htmlUrl;
    private String updatedAt;
    private boolean fork;

    private Instant fetchedAt;
}