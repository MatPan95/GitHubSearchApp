package org.example.githubsearchapp.validation;


import org.example.githubsearchapp.Exceptions.InvalidUserNameException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserNameValidationService {

    public void validateUser(String username){

        Optional.ofNullable(username)
                .filter(GitHubUserNameValidators.notEmptyOrLengthOutOfBounds())
                .filter(GitHubUserNameValidators.hasValidCharacters())
                .filter(GitHubUserNameValidators.notStartingOrEndingWithHyphen())
                .orElseThrow(InvalidUserNameException::new);

    }


}
