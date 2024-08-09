package org.example.githubsearchapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import lombok.SneakyThrows;
import org.example.githubsearchapp.Exceptions.UserNotFoundException;
import org.example.githubsearchapp.dataAccetion.model.Repo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AppIntegrationTest {

    @Autowired
    private AppController appController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String userName = "testUser";
    private String reposResponse;
    private String branchesResponse;
    private String response;

    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(
                    wireMockConfig()
                            .dynamicPort()
                            .usingFilesUnderClasspath("wiremock")
                            .asynchronousResponseEnabled(true)
            )
            .build();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("github.api.branches-url",() -> wireMockServer.baseUrl() + "/repos/{owner}/{repo}/branches");
        registry.add("github.api.repos-url",() -> wireMockServer.baseUrl() + "/users/{username}/repos");
    }

    @SneakyThrows
    @BeforeEach
    void setUp() {
        try (Stream<String> streamRepos = Files.lines(Paths.get("src/test/resources/dataAccetion/repos.json"))) {
            reposResponse = streamRepos.collect(Collectors.joining());
        }

        try (Stream<String> streamBranchesBeforeTest = Files.lines(Paths.get("src/test/resources/dataAccetion/branchesBeforeTest.json"))) {
            branchesResponse = streamBranchesBeforeTest.collect(Collectors.joining());
        }

        try (Stream<String> streamResponse = Files.lines(Paths.get("src/test/resources/response.json"))) {
            response = streamResponse.collect(Collectors.joining());
        }
    }

    @Test
    @SneakyThrows
    void testGetUserRepos()  {
        // given
        wireMockServer.stubFor(WireMock.get(WireMock.urlPathEqualTo("/users/testUser/repos"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(reposResponse)
                )
        );

        wireMockServer.stubFor(WireMock.get(WireMock.urlPathEqualTo("/repos/testUser/repo1/branches"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(branchesResponse)
                )
        );

        wireMockServer.stubFor(WireMock.get(WireMock.urlPathEqualTo("/repos/testUser/repo2/branches"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(branchesResponse)
                )
        );

        wireMockServer.stubFor(WireMock.get(WireMock.urlPathEqualTo("/repos/testUser/repo3/branches"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(branchesResponse)
                )
        );

        //when
        ResponseEntity<List<Repo>> responseEntity =  appController
                .getUserRepos(userName, Optional.of(MediaType.APPLICATION_JSON));

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(objectMapper.writeValueAsString(responseEntity.getBody()).hashCode())
                .as("Check if serverResponse and clientResponse are equal")
                .isEqualTo(response.hashCode());
    }

    @Test
    void testGetUserReposUserNotFound(){

        wireMockServer.stubFor(WireMock.get(WireMock.urlPathEqualTo("/users/testUser/repos"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(404)));

        assertThrows(UserNotFoundException.class, () ->
                appController.getUserRepos(userName, Optional.of(MediaType.APPLICATION_JSON)));
    }
}
