package org.example.githubsearchapp;

import lombok.AllArgsConstructor;
import org.example.githubsearchapp.dataAccetion.GitHubGetBranches;
import org.example.githubsearchapp.dataAccetion.GitHubGetRepos;
import org.example.githubsearchapp.dataAccetion.model.Repo;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class AppService {

    GitHubGetRepos gitHubGetRepos;
    GitHubGetBranches gitHubGetBranches;

    public List<Repo> getUserRepos(String userName) {

        List<Repo> repos = gitHubGetRepos.getReposData(userName).stream().parallel().filter(repo -> !repo.isFork()).toList();

        if(repos.isEmpty()) return repos;

        return gitHubGetBranches.getBranchesData(repos, userName);
    }
}

