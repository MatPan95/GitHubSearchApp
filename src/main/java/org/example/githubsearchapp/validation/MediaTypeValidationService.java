package org.example.githubsearchapp.validation;

import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpMediaTypeNotAcceptableException;

import java.util.Optional;

@Service
public class MediaTypeValidationService {

    @SneakyThrows
    public void validateMediaType(Optional<MediaType> mediaType) {
        mediaType
                .filter(value -> value.equals(MediaType.APPLICATION_JSON))
                .orElseThrow(() -> new HttpMediaTypeNotAcceptableException("Use media type: " + MediaType.APPLICATION_JSON));
    }

}
