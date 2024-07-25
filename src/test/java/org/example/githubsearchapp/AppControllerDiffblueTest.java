package org.example.githubsearchapp;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.example.githubsearchapp.validation.MediaTypeValidationService;
import org.example.githubsearchapp.validation.UserNameValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {AppController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class AppControllerDiffblueTest {
    @Autowired
    private AppController appController;

    @MockBean
    private AppService appService;

    @MockBean
    private MediaTypeValidationService mediaTypeValidationService;

    @MockBean
    private UserNameValidationService userNameValidationService;

    /**
     * Method under test: {@link AppController#getUserRepos(String, Optional)}
     */
    @Test
    void testGetUserRepos() throws Exception {
        // Arrange
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/{userName}", "janedoe")
                .header("Accept", "text/html, application/xhtml+xml, application/xml;q=0.9, */*;q=0.8");

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(appController).build().perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link AppController#getUserRepos(String, Optional)}
     */
    @Test
    void testGetUserRepos2() throws Exception {
        // Arrange
        doNothing().when(mediaTypeValidationService).validateMediaType(Mockito.<Optional<MediaType>>any());
        doNothing().when(userNameValidationService).validateUser(Mockito.<String>any());
        when(appService.getUserRepos(Mockito.<String>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/{userName}", "janedoe")
                .header("Accept", "");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(appController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }
}
