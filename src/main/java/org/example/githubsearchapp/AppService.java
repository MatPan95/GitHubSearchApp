package org.example.githubsearchapp;

import lombok.AllArgsConstructor;
import org.example.githubsearchapp.gitHubData.GitHubRestClient;
import org.example.githubsearchapp.gitHubData.model.Repo;
import org.springframework.stereotype.Service;
import java.util.List;
import static org.example.githubsearchapp.gitHubData.RepoUtils.isNotFork;

@Service
@AllArgsConstructor
public class AppService {

    GitHubRestClient gitHubRestClient;

    public List<Repo> getUserRepos(String userName) {

        List<Repo> repos = gitHubRestClient.getRepos(userName);

        return repos.isEmpty() ? repos : gitHubRestClient.getBranches(repos.parallelStream()
                .filter(isNotFork)
                .toList(), userName);
    }
}

