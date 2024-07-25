package org.example.githubsearchapp.gitHubData;

import lombok.AllArgsConstructor;
import org.example.githubsearchapp.Exceptions.UserNotFoundException;
import org.example.githubsearchapp.gitHubData.model.Repo;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class GitHubRestClient {

    private RestClient restClient;
    private GithubURIs githubURIs;

    public List<Repo> getRepos(String userName){
    return restClient.get()
            .uri(githubURIs.reposURI().replace("{username}", userName))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(status -> status.value() == 404, (request, response) -> {
                throw new UserNotFoundException();
            })
            .body(new ParameterizedTypeReference<>() {});

    }

    public List<Repo> getBranches(List<Repo> repos, String userName) {

        return repos.parallelStream().peek(repo -> repo.setBranches(
                restClient.get()
                        .uri(githubURIs.branchesURI()
                                .replace("{owner}", userName)
                                .replace("{repo}", repo.getRepositoryName()))
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .body(new ParameterizedTypeReference<>() {
                        })
        )).collect(Collectors.toList());



    }
}
