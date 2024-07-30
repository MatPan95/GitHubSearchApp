package org.example.githubsearchapp.dataAccetion;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.example.githubsearchapp.Exceptions.UserNotFoundException;
import org.example.githubsearchapp.dataAccetion.model.Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.List;


@AllArgsConstructor
@Component
@Setter
public class GitHubGetRepos implements getUserRepos {

    @Autowired
    private RestClient restClient;

    @Autowired
    private String githubReposURL;

    @Override
    public List<Repo> getReposData(String userName){
    return restClient.get()
            .uri(githubReposURL.replace("{username}", userName))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(status -> status.value() == 404, (request, response) -> {throw new UserNotFoundException();})
            .body(new ParameterizedTypeReference<>() {});

    }


}
