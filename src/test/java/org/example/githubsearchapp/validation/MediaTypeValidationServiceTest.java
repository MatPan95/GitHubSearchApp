package org.example.githubsearchapp.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class MediaTypeValidationServiceTest {

    private MediaTypeValidationService mediaTypeValidationService;

    @BeforeEach
    void setUp() {
        mediaTypeValidationService = new MediaTypeValidationService();
    }

    @Test
    void testValidateMediaType_withValidMediaType() {
        // Given
        Optional<MediaType> validMediaType = Optional.of(MediaType.APPLICATION_JSON);

        // When & Then (no exception should be thrown)
        mediaTypeValidationService.validateMediaType(validMediaType);
    }

    @Test
    void testValidateMediaType_withInvalidMediaType() {
        // Given
        Optional<MediaType> invalidMediaType = Optional.of(MediaType.TEXT_PLAIN);

        // When & Then
        assertThrows(HttpMediaTypeNotAcceptableException.class, () ->
                        mediaTypeValidationService.validateMediaType(invalidMediaType),
                "Use media type: application/json"
        );
    }

    @Test
    void testValidateMediaType_withEmptyMediaType() {
        // Given
        Optional<MediaType> emptyMediaType = Optional.empty();

        // When & Then
        assertThrows(HttpMediaTypeNotAcceptableException.class, () ->
                        mediaTypeValidationService.validateMediaType(emptyMediaType),
                "Use media type: application/json"
        );
    }
}
