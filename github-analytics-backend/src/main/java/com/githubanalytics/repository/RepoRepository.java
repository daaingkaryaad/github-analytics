package com.githubanalytics.repository;

import com.githubanalytics.entity.RepoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepoRepository extends JpaRepository<RepoEntity, Long> {

    List<RepoEntity> findByOwnerLogin(String ownerLogin);

    void deleteByOwnerLogin(String ownerLogin);
}