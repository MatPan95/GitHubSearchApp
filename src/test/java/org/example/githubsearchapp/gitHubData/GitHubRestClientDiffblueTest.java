package org.example.githubsearchapp.gitHubData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.example.githubsearchapp.Exceptions.UserNotFoundException;
import org.example.githubsearchapp.gitHubData.model.Branch;
import org.example.githubsearchapp.gitHubData.model.Repo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestClient;

@ContextConfiguration(classes = {GitHubRestClient.class, GithubURIs.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class GitHubRestClientDiffblueTest {
    @Autowired
    private GitHubRestClient gitHubRestClient;

    @MockBean
    private RestClient restClient;

    /**
     * Method under test: {@link GitHubRestClient#getRepos(String)}
     */
    @Test
    void testGetRepos() {
        // Arrange
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);
        ArrayList<Repo> repoList = new ArrayList<>();
        when(responseSpec.body(Mockito.<ParameterizedTypeReference<List<Repo>>>any())).thenReturn(repoList);
        RestClient.ResponseSpec responseSpec2 = mock(RestClient.ResponseSpec.class);
        when(responseSpec2.onStatus(Mockito.<Predicate<HttpStatusCode>>any(),
                Mockito.<RestClient.ResponseSpec.ErrorHandler>any())).thenReturn(responseSpec);
        RestClient.RequestBodySpec requestBodySpec = mock(RestClient.RequestBodySpec.class);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec2);
        RestClient.RequestBodySpec requestBodySpec2 = mock(RestClient.RequestBodySpec.class);
        when(requestBodySpec2.accept(isA(MediaType[].class))).thenReturn(requestBodySpec);
        RestClient.RequestHeadersUriSpec<RestClient.RequestBodySpec> requestHeadersUriSpec = mock(
                RestClient.RequestHeadersUriSpec.class);
        when(requestHeadersUriSpec.uri(Mockito.<String>any(), isA(Object[].class))).thenReturn(requestBodySpec2);
        Mockito.<RestClient.RequestHeadersUriSpec<?>>when(restClient.get()).thenReturn(requestHeadersUriSpec);

        // Act
        List<Repo> actualRepos = gitHubRestClient.getRepos("janedoe");

        // Assert
        verify(restClient).get();
        verify(requestBodySpec2).accept(isA(MediaType[].class));
        verify(requestBodySpec).retrieve();
        verify(responseSpec).body(isA(ParameterizedTypeReference.class));
        verify(responseSpec2).onStatus(isA(Predicate.class), isA(RestClient.ResponseSpec.ErrorHandler.class));
        verify(requestHeadersUriSpec).uri(eq("${github.api.repos-endpoint}"), isA(Object[].class));
        assertTrue(actualRepos.isEmpty());
        assertSame(repoList, actualRepos);
    }

    /**
     * Method under test: {@link GitHubRestClient#getRepos(String)}
     */
    @Test
    void testGetRepos2() {
        // Arrange
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);
        when(responseSpec.body(Mockito.<ParameterizedTypeReference<List<Repo>>>any()))
                .thenThrow(new UserNotFoundException());
        RestClient.ResponseSpec responseSpec2 = mock(RestClient.ResponseSpec.class);
        when(responseSpec2.onStatus(Mockito.<Predicate<HttpStatusCode>>any(),
                Mockito.<RestClient.ResponseSpec.ErrorHandler>any())).thenReturn(responseSpec);
        RestClient.RequestBodySpec requestBodySpec = mock(RestClient.RequestBodySpec.class);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec2);
        RestClient.RequestBodySpec requestBodySpec2 = mock(RestClient.RequestBodySpec.class);
        when(requestBodySpec2.accept(isA(MediaType[].class))).thenReturn(requestBodySpec);
        RestClient.RequestHeadersUriSpec<RestClient.RequestBodySpec> requestHeadersUriSpec = mock(
                RestClient.RequestHeadersUriSpec.class);
        when(requestHeadersUriSpec.uri(Mockito.<String>any(), isA(Object[].class))).thenReturn(requestBodySpec2);
        Mockito.<RestClient.RequestHeadersUriSpec<?>>when(restClient.get()).thenReturn(requestHeadersUriSpec);

        // Act and Assert
        assertThrows(UserNotFoundException.class, () -> gitHubRestClient.getRepos("janedoe"));
        verify(restClient).get();
        verify(requestBodySpec2).accept(isA(MediaType[].class));
        verify(requestBodySpec).retrieve();
        verify(responseSpec).body(isA(ParameterizedTypeReference.class));
        verify(responseSpec2).onStatus(isA(Predicate.class), isA(RestClient.ResponseSpec.ErrorHandler.class));
        verify(requestHeadersUriSpec).uri(eq("${github.api.repos-endpoint}"), isA(Object[].class));
    }

    /**
     * Method under test: {@link GitHubRestClient#getBranches(List, String)}
     */
    @Test
    void testGetBranches() {
        // Arrange, Act and Assert
        assertTrue(gitHubRestClient.getBranches(new ArrayList<>(), "janedoe").isEmpty());
    }

    /**
     * Method under test: {@link GitHubRestClient#getBranches(List, String)}
     */
    @Test
    void testGetBranches2() {
        // Arrange
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);
        when(responseSpec.body(Mockito.<ParameterizedTypeReference<List<Branch>>>any())).thenReturn(new ArrayList<>());
        RestClient.RequestBodySpec requestBodySpec = mock(RestClient.RequestBodySpec.class);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        RestClient.RequestBodySpec requestBodySpec2 = mock(RestClient.RequestBodySpec.class);
        when(requestBodySpec2.accept(isA(MediaType[].class))).thenReturn(requestBodySpec);
        RestClient.RequestHeadersUriSpec<RestClient.RequestBodySpec> requestHeadersUriSpec = mock(
                RestClient.RequestHeadersUriSpec.class);
        when(requestHeadersUriSpec.uri(Mockito.any(), isA(Object[].class))).thenReturn(requestBodySpec2);
        Mockito.<RestClient.RequestHeadersUriSpec<?>>when(restClient.get()).thenReturn(requestHeadersUriSpec);

        Repo repo = new Repo();
        repo.setBranches(new ArrayList<>());
        repo.setFork(true);
        repo.setRepositoryName("Repository Name");

        ArrayList<Repo> repos = new ArrayList<>();
        repos.add(repo);

        // Act
        List<Repo> actualBranches = gitHubRestClient.getBranches(repos, "janedoe");

        // Assert
        verify(restClient).get();
        verify(requestBodySpec2).accept(isA(MediaType[].class));
        verify(requestBodySpec).retrieve();
        verify(responseSpec).body(isA(ParameterizedTypeReference.class));
        verify(requestHeadersUriSpec).uri(eq("${github.api.branches-endpoint}"), isA(Object[].class));
        assertEquals(1, repos.size());
        assertEquals(repos, actualBranches);
        assertSame(repo, repos.get(0));
    }

    /**
     * Method under test: {@link GitHubRestClient#getBranches(List, String)}
     */
    @Test
    void testGetBranches3() {
        // Arrange
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);
        when(responseSpec.body(Mockito.<ParameterizedTypeReference<List<Branch>>>any()))
                .thenThrow(new UserNotFoundException());
        RestClient.RequestBodySpec requestBodySpec = mock(RestClient.RequestBodySpec.class);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        RestClient.RequestBodySpec requestBodySpec2 = mock(RestClient.RequestBodySpec.class);
        when(requestBodySpec2.accept(isA(MediaType[].class))).thenReturn(requestBodySpec);
        RestClient.RequestHeadersUriSpec<RestClient.RequestBodySpec> requestHeadersUriSpec = mock(
                RestClient.RequestHeadersUriSpec.class);
        when(requestHeadersUriSpec.uri(Mockito.any(), isA(Object[].class))).thenReturn(requestBodySpec2);
        Mockito.<RestClient.RequestHeadersUriSpec<?>>when(restClient.get()).thenReturn(requestHeadersUriSpec);

        Repo repo = new Repo();
        repo.setBranches(new ArrayList<>());
        repo.setFork(true);
        repo.setRepositoryName("Repository Name");

        ArrayList<Repo> repos = new ArrayList<>();
        repos.add(repo);

        // Act and Assert
        assertThrows(UserNotFoundException.class, () -> gitHubRestClient.getBranches(repos, "janedoe"));
        verify(restClient).get();
        verify(requestBodySpec2).accept(isA(MediaType[].class));
        verify(requestBodySpec).retrieve();
        verify(responseSpec).body(isA(ParameterizedTypeReference.class));
        verify(requestHeadersUriSpec).uri(eq("${github.api.branches-endpoint}"), isA(Object[].class));
    }

    /**
     * Method under test: {@link GitHubRestClient#getBranches(List, String)}
     */
    @Test
    void testGetBranches4() {
        // Arrange
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);
        when(responseSpec.body(Mockito.<ParameterizedTypeReference<List<Branch>>>any())).thenReturn(new ArrayList<>());
        RestClient.RequestBodySpec requestBodySpec = mock(RestClient.RequestBodySpec.class);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        RestClient.RequestBodySpec requestBodySpec2 = mock(RestClient.RequestBodySpec.class);
        when(requestBodySpec2.accept(isA(MediaType[].class))).thenReturn(requestBodySpec);
        RestClient.RequestHeadersUriSpec<RestClient.RequestBodySpec> requestHeadersUriSpec = mock(
                RestClient.RequestHeadersUriSpec.class);
        when(requestHeadersUriSpec.uri(Mockito.any(), isA(Object[].class))).thenReturn(requestBodySpec2);
        Mockito.<RestClient.RequestHeadersUriSpec<?>>when(restClient.get()).thenReturn(requestHeadersUriSpec);

        Repo repo = new Repo();
        repo.setBranches(new ArrayList<>());
        repo.setFork(true);
        repo.setRepositoryName("Repository Name");

        Repo repo2 = new Repo();
        repo2.setBranches(new ArrayList<>());
        repo2.setFork(false);
        repo2.setRepositoryName("{repo}");

        ArrayList<Repo> repos = new ArrayList<>();
        repos.add(repo2);
        repos.add(repo);

        // Act
        List<Repo> actualBranches = gitHubRestClient.getBranches(repos, "janedoe");

        // Assert
        verify(restClient, atLeast(1)).get();
        verify(requestBodySpec2, atLeast(1)).accept(isA(MediaType[].class));
        verify(requestBodySpec, atLeast(1)).retrieve();
        verify(responseSpec, atLeast(1)).body(isA(ParameterizedTypeReference.class));
        verify(requestHeadersUriSpec, atLeast(1)).uri(eq("${github.api.branches-endpoint}"), isA(Object[].class));
        assertEquals(2, repos.size());
        assertEquals(repos, actualBranches);
        assertSame(repo2, repos.get(0));
        assertSame(repo, repos.get(1));
    }
}
