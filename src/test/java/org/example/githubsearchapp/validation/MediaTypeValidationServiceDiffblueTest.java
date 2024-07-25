package org.example.githubsearchapp.validation;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.HttpMediaTypeNotAcceptableException;

@ContextConfiguration(classes = {MediaTypeValidationService.class})
@ExtendWith(SpringExtension.class)
class MediaTypeValidationServiceDiffblueTest {
    @Autowired
    private MediaTypeValidationService mediaTypeValidationService;

    /**
     * Method under test:
     * {@link MediaTypeValidationService#validateMediaType(Optional)}
     */
    @Test
    void testValidateMediaType() {
        // Arrange
        Optional<MediaType> mediaType = Optional.of(mock(MediaType.class));

        // Act and Assert
        assertThrows(HttpMediaTypeNotAcceptableException.class,
                () -> mediaTypeValidationService.validateMediaType(mediaType));
    }

    /**
     * Method under test:
     * {@link MediaTypeValidationService#validateMediaType(Optional)}
     */
    @Test
    void testValidateMediaType2() {
        // Arrange
        Optional<MediaType> mediaType = Optional.empty();

        // Act and Assert
        assertThrows(HttpMediaTypeNotAcceptableException.class,
                () -> mediaTypeValidationService.validateMediaType(mediaType));
    }
}
