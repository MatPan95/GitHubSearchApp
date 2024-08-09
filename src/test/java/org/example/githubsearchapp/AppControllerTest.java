package org.example.githubsearchapp;

import org.example.githubsearchapp.dataAccetion.model.Repo;
import org.example.githubsearchapp.validation.MediaTypeValidationService;
import org.example.githubsearchapp.validation.UserNameValidationService;
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

    @Test
    void testGetUserRepos() {
        // given

        String userName = "testUser";

        Repo repo1 = new Repo();
        Repo repo2 = new Repo();
        repo1.setRepositoryName("repository1");
        repo2.setRepositoryName("repository2");
        List<Repo> mockRepos = List.of(repo1, repo2);

        // when
        doNothing().when(mediaTypeValidationService).validateMediaType(Optional.of(MediaType.APPLICATION_JSON));
        doNothing().when(userNameValidationService).validateUser(userName);
        when(appService.getUserRepos(userName)).thenReturn(mockRepos);
        ResponseEntity<List<Repo>> response = appController.getUserRepos(userName, Optional.of(MediaType.APPLICATION_JSON));

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
