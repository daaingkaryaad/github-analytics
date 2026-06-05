package com.githubanalytics.mapper;

import com.githubanalytics.dto.RepoDTO;
import com.githubanalytics.dto.UserDTO;
import com.githubanalytics.entity.RepoEntity;
import com.githubanalytics.entity.UserEntity;

import java.time.Instant;

public final class GithubMapper {

    private GithubMapper() {
    }

    public static UserEntity toEntity(UserDTO dto) {
        return UserEntity.builder()
                .login(dto.getLogin())
                .name(dto.getName())
                .bio(dto.getBio())
                .avatarUrl(dto.getAvatarUrl())
                .htmlUrl(dto.getHtmlUrl())
                .followers(dto.getFollowers())
                .following(dto.getFollowing())
                .publicRepos(dto.getPublicRepos())
                .accountCreatedAt(dto.getCreatedAt())
                .fetchedAt(Instant.now())
                .build();
    }

    public static RepoEntity toEntity(String ownerLogin, RepoDTO dto) {
        return RepoEntity.builder()
                .id(dto.getId())
                .ownerLogin(ownerLogin)
                .name(dto.getName())
                .fullName(dto.getFullName())
                .description(dto.getDescription())
                .language(dto.getLanguage())
                .stars(dto.getStars())
                .forks(dto.getForks())
                .htmlUrl(dto.getHtmlUrl())
                .updatedAt(dto.getUpdatedAt())
                .fork(dto.isFork())
                .fetchedAt(Instant.now())
                .build();
    }
}