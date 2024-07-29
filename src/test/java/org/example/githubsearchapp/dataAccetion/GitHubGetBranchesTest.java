package org.example.githubsearchapp.dataAccetion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.githubsearchapp.AppConfig;
import org.example.githubsearchapp.TestConfig;
import org.example.githubsearchapp.dataAccetion.model.Branch;
import org.example.githubsearchapp.dataAccetion.model.Commit;
import org.example.githubsearchapp.dataAccetion.model.Repo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.UnorderedRequestExpectationManager;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(GitHubGetBranches.class)
@Import({AppConfig.class, TestConfig.class})
class GitHubGetBranchesTest {
    @Autowired
    private GitHubGetBranches gitHubGetBranches;
    @Autowired
    private GithubURIs githubUris;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockRestServiceServer mockServer = MockRestServiceServer.bindTo(new RestTemplate()).ignoreExpectOrder(true).build(new UnorderedRequestExpectationManager());

    @Test
    void getMoreThenOneBranch() throws JsonProcessingException {

        //given

        String userName = "user";
        String repoName1 = "repo1";

        Branch branch1 = new Branch("branch1", new Commit("sha1"));
        Branch branch2 = new Branch("branch2", new Commit("sha2"));
        List<Branch> branches1 = new ArrayList<>();
        branches1.add(branch1);
        branches1.add(branch2);

        Repo repo1 = new Repo();
        repo1.setRepositoryName(repoName1);

        List<Repo> repos = new ArrayList<>();
        repos.add(repo1);

        mockServer.expect(ExpectedCount.max(1), requestTo(githubUris.branchesURI().replace("{owner}", userName).replace("{repo}", repoName1)))
                .andRespond(withSuccess(mapper.writeValueAsString(branches1), MediaType.APPLICATION_JSON));

        //when

        List<Repo> response = gitHubGetBranches.getBranchesData(repos, userName);

        //then

        assertEquals(1, response.size(), "The response should contain 1 repos");
        assertEquals(repoName1, response.getFirst().getRepositoryName(), "The repo name should be " + repoName1);

        mockServer.verify();

    }

    @Test
    void getOneBranch() throws JsonProcessingException {

        //given

        String userName = "user";
        String repoName1 = "repo1";

        Branch branch1 = new Branch("branch1", new Commit("sha1"));
        List<Branch> branches1 = new ArrayList<>();
        branches1.add(branch1);

        mockServer.expect(ExpectedCount.max(1), requestTo(githubUris.branchesURI().replace("{owner}", userName).replace("{repo}", repoName1)))
                .andRespond(withSuccess(mapper.writeValueAsString(branches1), MediaType.APPLICATION_JSON));

        Repo repo1 = new Repo();
        repo1.setRepositoryName(repoName1);

        List<Repo> repos = new ArrayList<>();
        repos.add(repo1);

        //when

        List<Repo> response = gitHubGetBranches.getBranchesData(repos, userName);

        //then

        assertEquals(1, response.size(), "The response should contain 1 repos");
        assertEquals(repoName1, response.getFirst().getRepositoryName(), "The repo name should be " + repoName1);

        mockServer.verify();

    }

}
