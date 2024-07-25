package org.example.githubsearchapp;

import org.example.githubsearchapp.dataAccetion.GitHubGetBranches;
import org.example.githubsearchapp.dataAccetion.GitHubGetRepos;
import org.example.githubsearchapp.dataAccetion.GithubURIs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AppConfig {

    @Bean
    public RestClient restClient() {
        return RestClient.create();
    }

    @Bean
    public GitHubGetRepos gitHubGetRepos(RestClient restClient, GithubURIs githubURIs) {
        return new GitHubGetRepos(restClient, githubURIs);
    }

    @Bean
    public GitHubGetBranches gitHubGetBranches(RestClient restClient, GithubURIs githubURIs) {
        return new GitHubGetBranches(restClient, githubURIs);
    }


}
