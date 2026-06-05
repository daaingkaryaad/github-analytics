package com.githubanalytics.repository;

import com.githubanalytics.entity.RepoEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RepoRepositoryTest {

    @Autowired
    private RepoRepository repoRepository;

    @Test
    void findByOwnerLogin_returnsOnlyThatOwnersRepos() {
        repoRepository.save(repo(1L, "octocat", "hello"));
        repoRepository.save(repo(2L, "octocat", "world"));
        repoRepository.save(repo(3L, "torvalds", "linux"));

        List<RepoEntity> octocatRepos = repoRepository.findByOwnerLogin("octocat");

        assertThat(octocatRepos)
                .hasSize(2)
                .extracting(RepoEntity::getName)
                .containsExactlyInAnyOrder("hello", "world");
    }

    @Test
    void deleteByOwnerLogin_removesOnlyThatOwnersRepos() {
        repoRepository.save(repo(1L, "octocat", "hello"));
        repoRepository.save(repo(2L, "torvalds", "linux"));

        repoRepository.deleteByOwnerLogin("octocat");

        assertThat(repoRepository.findByOwnerLogin("octocat")).isEmpty();
        assertThat(repoRepository.findByOwnerLogin("torvalds")).hasSize(1);
    }

    private RepoEntity repo(Long id, String owner, String name) {
        return RepoEntity.builder()
                .id(id)
                .ownerLogin(owner)
                .name(name)
                .stars(0)
                .forks(0)
                .fork(false)
                .fetchedAt(Instant.now())
                .build();
    }
}
