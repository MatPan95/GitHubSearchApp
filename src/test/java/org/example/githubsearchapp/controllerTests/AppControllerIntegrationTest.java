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


        ObjectMapper objectMapper = new ObjectMapper();


        wireMockServer.stubFor(WireMock.get(WireMock.urlPathEqualTo("/users/testUser/repos"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsString(userName))));

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
