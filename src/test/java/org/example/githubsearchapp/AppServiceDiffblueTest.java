package org.example.githubsearchapp;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.example.githubsearchapp.gitHubData.GitHubRestClient;
import org.example.githubsearchapp.gitHubData.model.Repo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {AppService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class AppServiceDiffblueTest {
    @Autowired
    private AppService appService;

    @MockBean
    private GitHubRestClient gitHubRestClient;

    /**
     * Method under test: {@link AppService#getUserRepos(String)}
     */
    @Test
    void testGetUserRepos() {
        // Arrange
        ArrayList<Repo> repoList = new ArrayList<>();
        when(gitHubRestClient.getRepos(Mockito.<String>any())).thenReturn(repoList);

        // Act
        List<Repo> actualUserRepos = appService.getUserRepos("janedoe");

        // Assert
        verify(gitHubRestClient).getRepos(eq("janedoe"));
        assertTrue(actualUserRepos.isEmpty());
        assertSame(repoList, actualUserRepos);
    }

    /**
     * Method under test: {@link AppService#getUserRepos(String)}
     */
    @Test
    void testGetUserRepos2() {
        // Arrange
        Repo repo = new Repo();
        repo.setBranches(new ArrayList<>());
        repo.setFork(true);
        repo.setRepositoryName("Repository Name");

        ArrayList<Repo> repoList = new ArrayList<>();
        repoList.add(repo);
        ArrayList<Repo> repoList2 = new ArrayList<>();
        when(gitHubRestClient.getBranches(Mockito.<List<Repo>>any(), Mockito.<String>any())).thenReturn(repoList2);
        when(gitHubRestClient.getRepos(Mockito.<String>any())).thenReturn(repoList);

        // Act
        List<Repo> actualUserRepos = appService.getUserRepos("janedoe");

        // Assert
        verify(gitHubRestClient).getBranches(isA(List.class), eq("janedoe"));
        verify(gitHubRestClient).getRepos(eq("janedoe"));
        assertTrue(actualUserRepos.isEmpty());
        assertSame(repoList2, actualUserRepos);
    }
}
