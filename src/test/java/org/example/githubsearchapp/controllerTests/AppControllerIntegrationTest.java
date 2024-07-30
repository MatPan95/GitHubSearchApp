package org.example.githubsearchapp.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.example.githubsearchapp.AppController;
import org.example.githubsearchapp.dataAccetion.model.Branch;
import org.example.githubsearchapp.dataAccetion.model.Commit;
import org.example.githubsearchapp.dataAccetion.model.Owner;
import org.example.githubsearchapp.dataAccetion.model.Repo;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AppControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppController appController;

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

    @Test
    void testGetUserRepos() throws Exception {
        // given

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
        Repo repo3 = new Repo(); //fork

        repo1.setRepositoryName("repo1");
        repo1.setOwner(new Owner("login1"));
        repo1.setFork(false);

        repo2.setRepositoryName("repo2");
        repo2.setOwner(new Owner("login2"));
        repo2.setFork(false);

        repo3.setRepositoryName("repo3");
        repo3.setOwner(new Owner("login3"));
        repo3.setFork(true);

        List<Repo> repos = new ArrayList<>();
        repos.add(repo1);
        repos.add(repo2);
        repos.add(repo3);

        ObjectMapper objectMapper = new ObjectMapper();

        repos.removeIf(Repo::isFork);


        wireMockServer.stubFor(WireMock.get(WireMock.urlPathEqualTo("/users/testUser/repos"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsString(repos))));

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

        ResponseEntity<List<Repo>> responseEntity = appController.getUserRepos(userName, Optional.of(MediaType.APPLICATION_JSON));


        //then

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(Objects.requireNonNull(responseEntity.getBody()).size()).isEqualTo(2);

    }

    @Test
    void testGetUserReposEmptyRepos() throws Exception {
        // given

        String userName = "testUser";

        List<Repo> repos = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();

        wireMockServer.stubFor(WireMock.get(WireMock.urlPathEqualTo("/users/testUser/repos"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsString(repos))));

        //when

        ResponseEntity<List<Repo>> responseEntity = appController.getUserRepos(userName, Optional.of(MediaType.APPLICATION_JSON));


        //then

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(responseEntity.getBody()).size()).isEqualTo(0);

    }

    @Test
    void testGetUserReposUserNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/invalidUser")
                        .header("Accept", MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
