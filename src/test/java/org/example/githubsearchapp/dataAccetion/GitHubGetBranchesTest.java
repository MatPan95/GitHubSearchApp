package org.example.githubsearchapp.dataAccetion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.example.githubsearchapp.dataAccetion.model.Owner;
import org.example.githubsearchapp.dataAccetion.model.Repo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GitHubGetBranchesTest {

    @Autowired
    private GitHubGetBranches gitHubGetBranches;

    private final String userName = "testUser";
    private final Repo repo1 = new Repo();
    private final Repo repo2 = new Repo();
    private final List<Repo> repos = new ArrayList<>();
    private String serverResponse;
    private String clientResponseController;
    private final ObjectMapper objectMapper = new ObjectMapper();

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
    }

    @BeforeEach
    @SneakyThrows
    void setUp() {
        repo1.setRepositoryName("testRepo1");
        repo1.setOwner(new Owner(userName));
        repo1.setFork(false);

        repo2.setRepositoryName("testRepo2");
        repo2.setOwner(new Owner(userName));
        repo2.setFork(false);

        repos.add(repo1);
        repos.add(repo2);

        try (Stream<String> streamBranchesBeforeTest = Files.lines(Paths.get("src/test/resources/dataAccetion/branchesBeforeTest.json"))) {
            serverResponse = streamBranchesBeforeTest.collect(Collectors.joining());
        }
        try (Stream<String> streamBranchesAfterTest = Files.lines(Paths.get("src/test/resources/dataAccetion/branchesAfterTest.json"))) {
            clientResponseController = streamBranchesAfterTest.collect(Collectors.joining());
        }
    }

    @Test
    @SneakyThrows
    void getBranchesData() {

        //given
        wireMockServer.stubFor(WireMock.get(WireMock.urlPathEqualTo("/repos/testUser/testRepo1/branches"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(serverResponse)));

        wireMockServer.stubFor(WireMock.get(WireMock.urlPathEqualTo("/repos/testUser/testRepo2/branches"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(serverResponse)));

        //when
        List<Repo> clientResponse = gitHubGetBranches.getBranchesData(repos, userName);

        //then
        Assertions.assertThat(objectMapper.writeValueAsString(clientResponse).hashCode())
                .as("Check if serverResponse and clientResponse are equal")
                .isEqualTo(clientResponseController.hashCode());

    }
}
