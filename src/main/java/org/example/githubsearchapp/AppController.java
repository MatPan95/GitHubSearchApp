package org.example.githubsearchapp;

import lombok.AllArgsConstructor;
import org.example.githubsearchapp.gitHubDataAccetion.model.Repo;
import org.example.githubsearchapp.validation.MediaTypeValidationService;
import org.example.githubsearchapp.validation.UserNameValidationService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController()
@AllArgsConstructor
public class AppController {

    private final MediaTypeValidationService mediaTypeValidationService;
    private final UserNameValidationService userNameValidationService;
    private final AppService appService;

    @Cacheable(value = "userReposCache", key = "#userName")
    @GetMapping( value = "/{userName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Repo>> getUserRepos(@PathVariable String userName, @RequestHeader(value = "Accept", defaultValue = MediaType.APPLICATION_JSON_VALUE) Optional<MediaType> mediaType) {

        mediaTypeValidationService.validateMediaType(mediaType);
        userNameValidationService.validateUser(userName);





        return new ResponseEntity<>(appService.getUserRepos(userName), HttpStatus.OK);

    }
}
