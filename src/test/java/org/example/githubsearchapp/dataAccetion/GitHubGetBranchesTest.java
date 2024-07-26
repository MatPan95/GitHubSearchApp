package org.example.githubsearchapp.dataAccetion;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.example.githubsearchapp.dataAccetion.model.Branch;
import org.example.githubsearchapp.dataAccetion.model.Repo;
import org.example.githubsearchapp.dataAccetion.model.Commit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.ArrayList;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@WireMockTest(httpPort = 9090)
public class GitHubGetBranchesTest {

    private GitHubGetBranches gitHubGetBranches;

    @Mock
    private RestClient restClient = RestClient.create();

    @Mock
    private GithubURIs githubURIs = new GithubURIs("fdfdfd", "http://localhost:9090/repos/{owner}/{repo}/branches)");

    @BeforeEach
    public void setup() {
        gitHubGetBranches = new GitHubGetBranches(restClient, githubURIs);
    }

    @Test
    void testGetBranchesData() {
        // Mock repo data
        Repo repo = new Repo();
        repo.setRepositoryName("sample-repo");
        List<Repo> repos = new ArrayList<>();
        repos.add(repo);

        // Mock URI
        String branchesUri = "http://localhost:9090/repos/sample-user/sample-repo/branches";
  //      when(githubURIs.branchesURI()).thenReturn("http://localhost:9090/repos/{owner}/{repo}/branches");

        // Mock WireMock response
        String branchData = "[{\"name\": \"main\", \"commit\": {\"sha\": \"abc123\"}}]";
        stubFor(get(urlEqualTo("/repos/sample-user/sample-repo/branches"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Accept", "application/json")
                        .withBody(branchData)));

        // Mock restClient
        when(restClient.get()
                .uri("http://localhost:9090/repos/{owner}/{repo}/branches")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<List<Branch>>() {}))
                .thenReturn(List.of(new Branch("main", new Commit("abc123"))));

        // Execute the method
        List<Repo> result = gitHubGetBranches.getBranchesData(repos, "sample-user");

        // Verify the result using AssertJ
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getBranches()).hasSize(1);
        Branch branch = result.getFirst().getBranches().getFirst();
        assertThat(branch.getBranchName()).isEqualTo("main");
        assertThat(branch.getCommit().sha()).isEqualTo("abc123");
    }
}

