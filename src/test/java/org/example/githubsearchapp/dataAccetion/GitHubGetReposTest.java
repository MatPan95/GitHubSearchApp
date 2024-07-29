package org.example.githubsearchapp.dataAccetion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.githubsearchapp.AppConfig;
import org.example.githubsearchapp.TestConfig;
import org.example.githubsearchapp.dataAccetion.model.Owner;
import org.example.githubsearchapp.dataAccetion.model.Repo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(GitHubGetRepos.class)
@Import({AppConfig.class, TestConfig.class})
class GitHubGetReposTest {

    @Autowired
    private GitHubGetRepos gitHubGetRepos;
    @Autowired
    private GithubURIs githubUris;
    @Autowired
    private MockRestServiceServer server;
    @Autowired
    ObjectMapper mapper;

    @Test
    void getMoreThenOneReposData() throws JsonProcessingException {

        //given

        String userName = "user";

        Repo repo1 = new Repo();
        repo1.setOwner(new Owner(userName));
        repo1.setRepositoryName("repository1");
        repo1.setFork(false);

        Repo repo2 = new Repo();
        repo2.setOwner(new Owner(userName));
        repo2.setRepositoryName("repository2");
        repo2.setFork(false);

        List<Repo> repos = new ArrayList<>();
        repos.add(repo1);
        repos.add(repo2);


        server.expect(requestTo(githubUris.reposURI().replace("{username}", userName)))
                .andRespond(withSuccess(mapper.writeValueAsString(repos), MediaType.APPLICATION_JSON));

        //when

        List<Repo> response = gitHubGetRepos.getReposData(userName);

        //then

        assertThat(response).hasSize(2);
        assertThat(response.get(0).getRepositoryName()).isEqualTo("repository1");
        assertThat(response.get(1).getRepositoryName()).isEqualTo("repository2");
    }

    @Test
    void getOneReposData() throws JsonProcessingException {

        //given

        String userName = "user";

        Repo repo = new Repo();
        repo.setOwner(new Owner(userName));
        repo.setRepositoryName("repository1");
        repo.setFork(false);

        List<Repo> repos = new ArrayList<>();
        repos.add(repo);


        server.expect(requestTo(githubUris.reposURI().replace("{username}", userName)))
                .andRespond(withSuccess(mapper.writeValueAsString(repos), MediaType.APPLICATION_JSON));

        //when

        List<Repo> response = gitHubGetRepos.getReposData(userName);

        //then

        assertThat(response).hasSize(1);
        assertThat(response.getFirst().getRepositoryName()).isEqualTo("repository1");
    }

    @Test
    void getEmptyReposData() throws JsonProcessingException {

        //given

        String userName = "user";

        List<Repo> repos = new ArrayList<>();


        server.expect(requestTo(githubUris.reposURI().replace("{username}", userName)))
                .andRespond(withSuccess(mapper.writeValueAsString(repos), MediaType.APPLICATION_JSON));

        //when

        List<Repo> response = gitHubGetRepos.getReposData(userName);

        //then

        assertThat(response).hasSize(0);
    }
}
