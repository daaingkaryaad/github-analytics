package com.githubanalytics.service;

import com.githubanalytics.client.GithubApiClient;
import com.githubanalytics.dto.RepoDTO;
import com.githubanalytics.dto.UserDTO;
import com.githubanalytics.repository.RepoRepository;
import com.githubanalytics.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GithubServiceTest {

    @Mock
    private GithubApiClient client;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RepoRepository repoRepository;

    @InjectMocks
    private GithubService service;

    @Test
    void getUser_returnsUser_andPersistsSnapshot() {
        UserDTO dto = UserDTO.builder()
                .login("octocat")
                .name("The Octocat")
                .followers(100)
                .build();
        when(client.getUser("octocat")).thenReturn(dto);

        UserDTO result = service.getUser("octocat");

        assertThat(result.getLogin()).isEqualTo("octocat");
        verify(userRepository).save(argThat(e -> "octocat".equals(e.getLogin())));
    }

    @Test
    void getStats_computesAggregates_excludingForksWhereAppropriate() {
        List<RepoDTO> repos = List.of(
                RepoDTO.builder().id(1L).name("a").language("Java").stars(10).fork(false).build(),
                RepoDTO.builder().id(2L).name("b").language("Java").stars(5).fork(false).build(),
                RepoDTO.builder().id(3L).name("c").language("Go").stars(50).fork(true).build(),
                RepoDTO.builder().id(4L).name("d").language(null).stars(1).fork(false).build());
        when(client.getRepos("octocat")).thenReturn(repos);

        Map<String, Object> stats = service.getStats("octocat");

        assertThat(stats.get("totalRepos")).isEqualTo(4);
        assertThat(stats.get("totalStars")).isEqualTo(66);

        @SuppressWarnings("unchecked")
        Map<String, Long> languages = (Map<String, Long>) stats.get("languages");
        assertThat(languages).containsEntry("Java", 2L);
        assertThat(languages).doesNotContainKey("Go");

        RepoDTO top = (RepoDTO) stats.get("topRepo");
        assertThat(top.getName()).isEqualTo("a");
    }
}
