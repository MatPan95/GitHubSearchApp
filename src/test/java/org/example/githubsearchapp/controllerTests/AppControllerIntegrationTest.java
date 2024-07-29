package org.example.githubsearchapp.controllerTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.githubsearchapp.AppConfig;
import org.example.githubsearchapp.AppController;
import org.example.githubsearchapp.TestConfig;
import org.example.githubsearchapp.dataAccetion.model.Repo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@RestClientTest(AppController.class)
@Import({AppConfig.class, TestConfig.class})
public class AppControllerIntegrationTest {

    @Autowired
    private ObjectMapper mapper;

    private MockRestServiceServer server;

    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
        server = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void testGetUserRepos() throws JsonProcessingException {
        // Given
        String userName = "testuser";

        Repo repo1 = new Repo();
        repo1.setRepositoryName("Repo1");
        Repo repo2 = new Repo();
        repo2.setRepositoryName("Repo2");
        List<Repo> repos = new ArrayList<>();
        repos.add(repo1);
        repos.add(repo2);

        server.expect(requestTo("/repos/" + userName))
                .andRespond(withSuccess(mapper.writeValueAsString(repos), MediaType.APPLICATION_JSON));

        server.expect(requestTo("/branches/" + userName))
                .andRespond(withSuccess(mapper.writeValueAsString(repos), MediaType.APPLICATION_JSON));

        // When
        ResponseEntity<List> response = restTemplate.exchange(
                "http://localhost:8080/" + userName,
                HttpMethod.GET,
                null,
                List.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    void testGetUserRepos_invalidMediaType() {
        // Given
        String userName = "testuser";

        server.expect(requestTo("/repos/" + userName))
                .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

        // When
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:8080/" + userName,
                HttpMethod.GET,
                null,
                String.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
    }

    @Test
    void testGetUserRepos_invalidUsername() {
        // Given
        String userName = "invaliduser";

        server.expect(requestTo("/repos/" + userName))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        // When
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:8080/" + userName,
                HttpMethod.GET,
                null,
                String.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
