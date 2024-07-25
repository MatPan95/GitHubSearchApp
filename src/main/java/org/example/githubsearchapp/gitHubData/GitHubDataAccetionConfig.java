package org.example.githubsearchapp.gitHubData;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class GitHubDataAccetionConfig {

    @Bean
    public RestClient restClient() {
        return RestClient.create();
    }

}
