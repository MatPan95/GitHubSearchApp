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

        Branch branch1 = new Branch();
        branch1.setBranchName("branch1");
        branch1.setCommit(new Commit("sha1"));

        Branch branch2 = new Branch();
        branch2.setBranchName("branch2");
        branch2.setCommit(new Commit("sha2"));

        List<Branch> branches = new ArrayList<>();
        branches.add(branch1);
        branches.add(branch2);

        Repo repo = new Repo();
        repo.setRepositoryName("repo");

        List<Repo> repos = new ArrayList<>(Collections.singleton(repo));

        ObjectMapper objectMapper = new ObjectMapper();

        wireMockServer.stubFor(WireMock.get(WireMock.urlPathEqualTo("/repos/testUser/repo/branches"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsString(branches))));

        //when
        List<Repo> response = gitHubGetBranches.getBranchesData(repos, userName);

        //then
        assertThat(response).hasSize(1);
        assertThat(response.getFirst().getRepositoryName()).isEqualTo("repo");
        assertThat(response.getFirst().getBranches().getFirst().getBranchName()).isEqualTo("branch1");
        assertThat(response.getFirst().getBranches().get(1).getBranchName()).isEqualTo("branch2");

    }

    @Test
    void getBranchesDataForTwoRepos() throws JsonProcessingException {

        //given
        String userName = "testUser";

        Branch branch1 = new Branch();
        branch1.setBranchName("branch1");
        branch1.setCommit(new Commit("sha1"));

        Branch branch2 = new Branch();
        branch2.setBranchName("branch2");
        branch2.setCommit(new Commit("sha2"));

        List<Branch> branches1 = new ArrayList<>();
        branches1.add(branch1);
        branches1.add(branch2);

        Branch branch3 = new Branch();
        branch3.setBranchName("branch3");
        branch3.setCommit(new Commit("sha3"));

        Branch branch4 = new Branch();
        branch4.setBranchName("branch4");
        branch4.setCommit(new Commit("sha4"));

        List<Branch> branches2 = new ArrayList<>();
        branches2.add(branch3);
        branches2.add(branch4);

        Repo repo1 = new Repo();
        Repo repo2 = new Repo();
        repo1.setRepositoryName("repo1");
        repo2.setRepositoryName("repo2");

        List<Repo> repos = new ArrayList<>();
        repos.add(repo1);
        repos.add(repo2);

        ObjectMapper objectMapper = new ObjectMapper();

        wireMockServer.stubFor(WireMock.get(WireMock.urlPathEqualTo("/repos/testUser/repo1/branches"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsString(branches1))));

        wireMockServer.stubFor(WireMock.get(WireMock.urlPathEqualTo("/repos/testUser/repo2/branches"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsString(branches2))));

        //when
        List<Repo> response = gitHubGetBranches.getBranchesData(repos, userName);

        //then
        assertThat(response).hasSize(2);
        assertThat(response.getFirst().getRepositoryName()).isEqualTo("repo1");
        assertThat(response.getFirst().getBranches().getFirst().getBranchName()).isEqualTo("branch1");
        assertThat(response.getFirst().getBranches().get(1).getBranchName()).isEqualTo("branch2");
        assertThat(response.get(1).getRepositoryName()).isEqualTo("repo2");
        assertThat(response.get(1).getBranches().getFirst().getBranchName()).isEqualTo("branch3");
        assertThat(response.get(1).getBranches().get(1).getBranchName()).isEqualTo("branch4");

    }


}
