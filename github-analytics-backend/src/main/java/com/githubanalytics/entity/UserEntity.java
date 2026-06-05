package com.githubanalytics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "github_users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    private String login;

    private String name;

    @Column(length = 1000)
    private String bio;

    private String avatarUrl;
    private String htmlUrl;
    private int followers;
    private int following;
    private int publicRepos;

    /** GitHub account creation timestamp (ISO-8601 from the API). */
    private String accountCreatedAt;

    /** When this snapshot was last refreshed in our DB. */
    private Instant fetchedAt;
}