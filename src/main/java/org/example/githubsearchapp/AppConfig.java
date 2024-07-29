package org.example.githubsearchapp;

import org.example.githubsearchapp.dataAccetion.GitHubGetBranches;
import org.example.githubsearchapp.dataAccetion.GitHubGetRepos;
import org.example.githubsearchapp.dataAccetion.GithubURIs;
import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class AppConfig {

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder.build();
    }

    @Bean
    public GithubURIs githubURIs() {
        return new GithubURIs("https://api.github.com/users/{username}/repos", "https://api.github.com/repos/{owner}/{repo}/branches");
    }

    @Bean
    public GitHubGetRepos gitHubGetRepos(RestClient restClient, GithubURIs githubURIs) {
        return new GitHubGetRepos(restClient, githubURIs);
    }

    @Bean
    public GitHubGetBranches gitHubGetBranches(RestClient restClient, GithubURIs githubURIs) {
        return new GitHubGetBranches(restClient, githubURIs);
    }

    @Bean
    public RestClientCustomizer restClientCustomizer() {
        return (restClientBuilder) -> restClientBuilder
                .requestFactory(new JdkClientHttpRequestFactory());
    }

}
