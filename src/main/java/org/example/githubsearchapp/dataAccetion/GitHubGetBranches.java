package org.example.githubsearchapp.dataAccetion;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.example.githubsearchapp.dataAccetion.model.Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
@Setter
public class GitHubGetBranches implements getRepoBranches {

    @Autowired
    private RestClient restClient;

    @Autowired
    private String githubBranchesURL;


    @Override
    public List<Repo> getBranchesData(List<Repo> repos, String userName) {

        return repos.parallelStream().peek(repo -> repo.setBranches(
                restClient.get()
                        .uri(githubBranchesURL
                                .replace("{owner}", userName)
                                .replace("{repo}", repo.getRepositoryName()))
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .body(new ParameterizedTypeReference<>() {})
                )).collect(Collectors.toList());

    }
}
