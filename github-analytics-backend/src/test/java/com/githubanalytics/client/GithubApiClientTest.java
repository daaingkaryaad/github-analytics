package com.githubanalytics.client;

import com.githubanalytics.dto.UserDTO;
import com.githubanalytics.exception.GithubUserNotFoundException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GithubApiClientTest {

    private MockWebServer server;
    private GithubApiClient client;

    @BeforeEach
    void setUp() throws IOException {
        server = new MockWebServer();
        server.start();

        WebClient webClient = WebClient.builder()
                .baseUrl(server.url("/").toString())
                .build();
        client = new GithubApiClient(webClient);
        ReflectionTestUtils.setField(client, "token", "");
    }

    @AfterEach
    void tearDown() throws IOException {
        server.shutdown();
    }

    @Test
    void getUser_mapsJsonResponseToDto() {
        server.enqueue(new MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("{\"login\":\"octocat\",\"name\":\"The Octocat\","
                        + "\"followers\":100,\"public_repos\":8}"));

        UserDTO user = client.getUser("octocat");

        assertThat(user.getLogin()).isEqualTo("octocat");
        assertThat(user.getName()).isEqualTo("The Octocat");
        assertThat(user.getFollowers()).isEqualTo(100);
        assertThat(user.getPublicRepos()).isEqualTo(8);
    }

    @Test
    void getUser_on404_throwsUserNotFound() {
        server.enqueue(new MockResponse().setResponseCode(404).setBody("{}"));

        assertThatThrownBy(() -> client.getUser("ghost"))
                .isInstanceOf(GithubUserNotFoundException.class)
                .hasMessageContaining("ghost");
    }
}
