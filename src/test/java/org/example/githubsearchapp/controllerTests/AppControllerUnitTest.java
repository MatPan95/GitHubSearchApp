package org.example.githubsearchapp.controllerTests;


import org.example.githubsearchapp.AppController;
import org.example.githubsearchapp.AppService;
import org.example.githubsearchapp.dataAccetion.model.Repo;
import org.example.githubsearchapp.validation.MediaTypeValidationService;
import org.example.githubsearchapp.validation.UserNameValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AppControllerUnitTest {

    @Mock
    MediaTypeValidationService mediaTypeValidationService;

    @Mock
    UserNameValidationService userNameValidationService;

    @Mock
    AppService appService;

    @InjectMocks
    AppController appController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserRepos() {
        // given
        Repo repo = new Repo();
        repo.setRepositoryName("repo");
        List<Repo> repos = Collections.singletonList(repo);

        // when
        doNothing().when(mediaTypeValidationService).validateMediaType(Optional.of(MediaType.APPLICATION_JSON));
        doNothing().when(userNameValidationService).validateUser("john");
        when(appService.getUserRepos("john")).thenReturn(repos);

        ResponseEntity<List<Repo>> response = appController.getUserRepos("john", Optional.of(MediaType.APPLICATION_JSON));

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(repos, response.getBody());
    }

    @Test
    void testGetUserReposWithEmptyRepoList(){

        // given
        List<Repo> repos = List.of();

        // when
        doNothing().when(mediaTypeValidationService).validateMediaType(Optional.of(MediaType.APPLICATION_JSON));
        doNothing().when(userNameValidationService).validateUser("xxxx");
        when(appService.getUserRepos(anyString())).thenReturn(repos);
        ResponseEntity<List<Repo>> response = appController.getUserRepos("john", Optional.of(MediaType.APPLICATION_JSON));

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(repos, response.getBody());

    }

}
