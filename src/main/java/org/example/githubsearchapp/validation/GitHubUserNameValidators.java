package org.example.githubsearchapp.validation;

import java.util.function.Predicate;

class GitHubUserNameValidators {
    static Predicate<String> notEmptyOrLengthOutOfBounds() {
        return username -> !username.isEmpty() && username.length() <= 39;
    }

    static Predicate<String> hasValidCharacters() {
        return username -> username.matches("^[a-zA-Z0-9-]+$");
    }

    static Predicate<String> notStartingOrEndingWithHyphen() {
        return username -> !(username.startsWith("-") || username.endsWith("-"));
    }
}
