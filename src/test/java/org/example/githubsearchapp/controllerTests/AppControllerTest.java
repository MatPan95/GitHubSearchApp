package org.example.githubsearchapp.controllerTests;

import org.example.githubsearchapp.AppController;
import org.example.githubsearchapp.AppService;
import org.example.githubsearchapp.dataAccetion.model.Repo;
import org.example.githubsearchapp.validation.MediaTypeValidationService;
import org.example.githubsearchapp.validation.UserNameValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppControllerTest {

    @InjectMocks
    private AppController appController;

    @Mock
    private MediaTypeValidationService mediaTypeValidationService;

    @Mock
    private UserNameValidationService userNameValidationService;

    @Mock
    private AppService appService;

    private List<Repo> mockRepos;

    @BeforeEach
    public void setUp() {
        Repo repo1 = new Repo();
        Repo repo2 = new Repo();
        repo1.setRepositoryName("repository1");
        repo2.setRepositoryName("repository2");
        mockRepos = List.of(repo1, repo2);
    }

    @Test
    void testGetUserRepos() {
        // given
        String username = "Dick";

        // when
        doNothing().when(mediaTypeValidationService).validateMediaType(Optional.of(MediaType.APPLICATION_JSON));
        doNothing().when(userNameValidationService).validateUser(username);
        when(appService.getUserRepos(username)).thenReturn(mockRepos);
        ResponseEntity<List<Repo>> response = appController.getUserRepos(username, Optional.of(MediaType.APPLICATION_JSON));

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody()).isEqualTo(mockRepos);

        // Additional assertions for debugging
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody().get(0).getRepositoryName()).isEqualTo("repository1");
        assertThat(response.getBody().get(1).getRepositoryName()).isEqualTo("repository2");
    }
}
