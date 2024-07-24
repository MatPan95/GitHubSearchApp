package org.example.githubsearchapp.gitHubDataAccetion;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public record GithubURIs(@Value("${github.api.repos-endpoint}") String reposURI, @Value("${github.api.branches-endpoint}") String branchesURI) { }
