package org.example.githubsearchapp.dataAccetion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.example.githubsearchapp.Exceptions.UserNotFoundException;
import org.example.githubsearchapp.dataAccetion.model.Repo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GitHubGetReposTest {

    @Autowired
    private GitHubGetRepos gitHubGetRepos;

    private final String userName = "testUser";
    private final ObjectMapper objectMapper = new ObjectMapper();

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
    @SneakyThrows
    void getReposData() {

        //given
        String serverResponse;
        try (Stream<String> streamRepos = Files.lines(Paths.get("src/test/resources/dataAccetion/repos.json"))) {
            serverResponse = streamRepos.collect(Collectors.joining());
        }

        wireMockServer.stubFor(WireMock.get(WireMock.urlPathEqualTo("/users/testUser/repos"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(serverResponse)));

        //when
        List<Repo> clientResponse = gitHubGetRepos.getReposData(userName);

        //then
        Assertions.assertThat(objectMapper.writeValueAsString(clientResponse).hashCode())
                .as("Check if serverResponse and clientResponse are equal")
                .isEqualTo(serverResponse.hashCode());

    }

    @Test
    void throwingUserNotFoundException() {

        //given
        wireMockServer.stubFor(WireMock.get(WireMock.urlPathEqualTo("/users/testUser/repos"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(404)));

        // when & given
        assertThatThrownBy(() -> gitHubGetRepos.getReposData(userName))
                .isInstanceOf(UserNotFoundException.class);
    }


}
