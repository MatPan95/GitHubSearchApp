package org.example.githubsearchapp;

import lombok.AllArgsConstructor;
import org.example.githubsearchapp.Exceptions.UserNotFoundException;
import org.example.githubsearchapp.gitHubDataAccetion.GithubURIs;
import org.example.githubsearchapp.gitHubDataAccetion.model.Repo;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@AllArgsConstructor
public class AppService {

    RestClient restClient;
    GithubURIs githubURIs;

    public List<Repo> getUserRepos(String userName){

        List<Repo> repos = restClient.get()
                .uri(githubURIs.reposURI().replace("{username}", userName))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(status -> status.value() == 404, (request, response) -> {
                    throw new UserNotFoundException("User not found");
                })
                .body(new ParameterizedTypeReference<>() {});


        if(repos.isEmpty()) {

            return repos;
        }


        List<Repo> filteredRepo = repos.parallelStream().filter(repo -> !repo.isFork()).toList();


        for(Repo repo : filteredRepo){
            repo.setBranches(restClient.get()
                    .uri(githubURIs.branchesURI()
                            .replace("{owner}", userName)
                            .replace("{repo}", repo.getRepositoryName()))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {}));
        }


        return filteredRepo;


    }
}
