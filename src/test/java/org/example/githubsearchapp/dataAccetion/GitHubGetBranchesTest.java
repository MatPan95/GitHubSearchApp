package org.example.githubsearchapp.dataAccetion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.example.githubsearchapp.dataAccetion.model.Owner;
import org.example.githubsearchapp.dataAccetion.model.Repo;
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

    @Test
    @SneakyThrows
    void getBranchesData() {

        //given
        String userName = "testUser";

        Repo repo1 = new Repo();
        repo1.setRepositoryName("testRepo1");
        repo1.setOwner(new Owner(userName));
        repo1.setFork(false);

        Repo repo2 = new Repo();
        repo2.setRepositoryName("testRepo2");
        repo2.setOwner(new Owner(userName));
        repo2.setFork(false);

        List<Repo> repos = new ArrayList<>();
        repos.add(repo1);
        repos.add(repo2);

        Stream<String> stream1 = Files.lines(Paths.get("src/test/resources/dataAccetion/branchesBeforeTest.json"));
        String serverResponse = stream1.collect(Collectors.joining());
        stream1.close();

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
        Stream<String> stream2 = Files.lines(Paths.get("src/test/resources/dataAccetion/branchesAfterTest.json"));
        String clientResponseController = stream2.collect(Collectors.joining());
        stream1.close();

        ObjectMapper objectMapper = new ObjectMapper();

        Assertions.assertThat(objectMapper.writeValueAsString(clientResponse).hashCode())
                .as("Check if serverResponse and clientResponse are equal")
                .isEqualTo(clientResponseController.hashCode());

    }
}
