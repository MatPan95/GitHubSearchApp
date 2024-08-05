package org.example.githubsearchapp;

import org.example.githubsearchapp.dataAccetion.GitHubGetBranches;
import org.example.githubsearchapp.dataAccetion.GitHubGetRepos;
import org.example.githubsearchapp.dataAccetion.model.Repo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = AppServiceTest.class)
class AppServiceTest {

    @Mock
    private GitHubGetRepos gitHubGetRepos;

    @Mock
    private GitHubGetBranches gitHubGetBranches;

    @InjectMocks
    private AppService appService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserRepos() {
        // Given
        String userName = "testUser";
        List<Repo> repos = new ArrayList<>();
        Repo repo1 = new Repo();
        repo1.setFork(false);
        Repo repo2 = new Repo();
        repo2.setFork(false);
        repos.add(repo1);
        repos.add(repo2);

        // When
        when(gitHubGetRepos.getReposData(userName)).thenReturn(repos);
        when(gitHubGetBranches.getBranchesData(repos, userName)).thenReturn(repos);
        List<Repo> result = appService.getUserRepos(userName);

        // Then
        assertThat(result).hasSize(2);
        verify(gitHubGetRepos, times(1)).getReposData(userName);
        verify(gitHubGetBranches, times(1)).getBranchesData(repos, userName);
    }

    @Test
    void testGetUserRepos_withNoRepos() {
        // Given
        String userName = "testUser";
        List<Repo> repos = new ArrayList<>();

        when(gitHubGetRepos.getReposData(userName)).thenReturn(repos);

        // When
        List<Repo> result = appService.getUserRepos(userName);

        // Then
        assertThat(result).isEmpty();
        verify(gitHubGetRepos, times(1)).getReposData(userName);
        verify(gitHubGetBranches, never()).getBranchesData(anyList(), anyString());
    }
}
