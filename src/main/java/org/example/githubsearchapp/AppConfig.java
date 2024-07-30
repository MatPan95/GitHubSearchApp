package org.example.githubsearchapp;

import org.example.githubsearchapp.dataAccetion.GitHubGetBranches;
import org.example.githubsearchapp.dataAccetion.GitHubGetRepos;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class AppConfig {

    @Value("${github.api.branches-url}")
    private String githubBranchesEndpoint;

    @Value("${github.api.repos-url}")
    private String githubReposEndpoint;

    @Bean
    public String githubBranchesURL() {
        return githubBranchesEndpoint;
    }

    @Bean
    public String githubReposURL() {
        return githubReposEndpoint;
    }

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder.build();
    }

    @Bean
    public GitHubGetRepos gitHubGetRepos(RestClient restClient, String githubReposURL) {
        return new GitHubGetRepos(restClient, githubReposURL);
    }

    @Bean
    public GitHubGetBranches gitHubGetBranches(RestClient restClient,String githubBranchesURL) {
        return new GitHubGetBranches(restClient, githubBranchesURL);
    }

    @Bean
    public RestClientCustomizer restClientCustomizer() {
        return (restClientBuilder) -> restClientBuilder
                .requestFactory(new JdkClientHttpRequestFactory());
    }

}
