package org.example.githubsearchapp.dataAccetion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.example.githubsearchapp.Exceptions.UserNotFoundException;
import org.example.githubsearchapp.dataAccetion.model.Owner;
import org.example.githubsearchapp.dataAccetion.model.Repo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GitHubGetReposTest {

    @Autowired
    private GitHubGetRepos gitHubGetRepos;

    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(
                    wireMockConfig()
                            .dynamicPort()
                            .usingFilesUnderClasspath("wiremock")
            )
            .build();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("github.api.repos-url",() -> wireMockServer.baseUrl() + "/users/{username}/repos");
    }

    @Test
    void getReposData() throws JsonProcessingException {

        //given
        String userName = "testUser";

        Repo repo1 = new Repo();
        repo1.setRepositoryName("repository1");
        repo1.setOwner(new Owner("owner1"));
        repo1.setFork(false);

        Repo repo2 = new Repo();
        repo2.setRepositoryName("repository2");
        repo2.setOwner(new Owner("owner2"));
        repo2.setFork(false);

        List<Repo> repos = new ArrayList<>();
        repos.add(repo1);
        repos.add(repo2);

        ObjectMapper objectMapper = new ObjectMapper();

        wireMockServer.stubFor(WireMock.get(WireMock.urlPathEqualTo("/users/testUser/repos"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsString(repos))));

        //when
        List<Repo> response = gitHubGetRepos.getReposData(userName);

        //then
        assertThat(response).hasSize(2);
        assertThat(response.getFirst().getRepositoryName()).isEqualTo("repository1");
    }

    @Test
    void throwingUserNotFoundException() {

        //given
        String userName = "testUser";

        wireMockServer.stubFor(WireMock.get(WireMock.urlPathEqualTo("/users/testUser/repos"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(404)));

        // when & given
        assertThatThrownBy(() -> gitHubGetRepos.getReposData(userName))
                .isInstanceOf(UserNotFoundException.class);
    }


}
