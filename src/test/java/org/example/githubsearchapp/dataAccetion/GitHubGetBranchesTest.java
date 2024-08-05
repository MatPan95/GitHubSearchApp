package org.example.githubsearchapp.dataAccetion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.example.githubsearchapp.dataAccetion.model.Branch;
import org.example.githubsearchapp.dataAccetion.model.Commit;
import org.example.githubsearchapp.dataAccetion.model.Repo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

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
    void getBranchesData() throws JsonProcessingException {

        //given
        String userName = "testUser";


        ObjectMapper objectMapper = new ObjectMapper();

        wireMockServer.stubFor(WireMock.get(WireMock.urlPathEqualTo("/repos/testUser/repo/branches"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsString(userName))));

        //when
        List<Repo> response = gitHubGetBranches.getBranchesData(List.of(), userName);

        //then

    }

    @Test
    void getBranchesDataForTwoRepos() throws JsonProcessingException {

        //given
        String userName = "testUser";


        ObjectMapper objectMapper = new ObjectMapper();

        wireMockServer.stubFor(WireMock.get(WireMock.urlPathEqualTo("/repos/testUser/repo1/branches"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsString(userName))));

        wireMockServer.stubFor(WireMock.get(WireMock.urlPathEqualTo("/repos/testUser/repo2/branches"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsString(userName))));

        //when
        List<Repo> response = gitHubGetBranches.getBranchesData(List.of(), userName);

        //then

    }


}
