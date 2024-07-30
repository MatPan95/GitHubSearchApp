package org.example.githubsearchapp.validation;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GitHubUserNameValidatorsTest {

    @Test
    void testNotEmptyOrLengthOutOfBounds() {
        // Valid cases
        assertThat(GitHubUserNameValidators.notEmptyOrLengthOutOfBounds().test("validUsername")).isTrue();
        assertThat(GitHubUserNameValidators.notEmptyOrLengthOutOfBounds().test("user")).isTrue();

        // Invalid cases
        assertThat(GitHubUserNameValidators.notEmptyOrLengthOutOfBounds().test("")).isFalse();
        assertThat(GitHubUserNameValidators.notEmptyOrLengthOutOfBounds().test("thisusernameiswaytoolongtobeacceptedbygithubsinceithasmorethan39characters")).isFalse();
    }

    @Test
    void testHasValidCharacters() {
        // Valid cases
        assertThat(GitHubUserNameValidators.hasValidCharacters().test("valid-Username")).isTrue();
        assertThat(GitHubUserNameValidators.hasValidCharacters().test("username123")).isTrue();

        // Invalid cases
        assertThat(GitHubUserNameValidators.hasValidCharacters().test("invalid_username")).isFalse();
        assertThat(GitHubUserNameValidators.hasValidCharacters().test("username@123")).isFalse();
    }

    @Test
    void testNotStartingOrEndingWithHyphen() {
        // Valid cases
        assertThat(GitHubUserNameValidators.notStartingOrEndingWithHyphen().test("valid-Username")).isTrue();
        assertThat(GitHubUserNameValidators.notStartingOrEndingWithHyphen().test("user-name")).isTrue();

        // Invalid cases
        assertThat(GitHubUserNameValidators.notStartingOrEndingWithHyphen().test("-username")).isFalse();
        assertThat(GitHubUserNameValidators.notStartingOrEndingWithHyphen().test("username-")).isFalse();
    }

    @Test
    void testCombinedValidators() {
        // Valid case
        assertThat(GitHubUserNameValidators.notEmptyOrLengthOutOfBounds()
                .and(GitHubUserNameValidators.hasValidCharacters())
                .and(GitHubUserNameValidators.notStartingOrEndingWithHyphen())
                .test("valid-Username")).isTrue();

        // Invalid case: empty
        assertThat(GitHubUserNameValidators.notEmptyOrLengthOutOfBounds()
                .and(GitHubUserNameValidators.hasValidCharacters())
                .and(GitHubUserNameValidators.notStartingOrEndingWithHyphen())
                .test("")).isFalse();

        // Invalid case: too long
        assertThat(GitHubUserNameValidators.notEmptyOrLengthOutOfBounds()
                .and(GitHubUserNameValidators.hasValidCharacters())
                .and(GitHubUserNameValidators.notStartingOrEndingWithHyphen())
                .test("thisusernameiswaytoolongtobeacceptedbygithubsinceithasmorethan39characters")).isFalse();

        // Invalid case: invalid characters
        assertThat(GitHubUserNameValidators.notEmptyOrLengthOutOfBounds()
                .and(GitHubUserNameValidators.hasValidCharacters())
                .and(GitHubUserNameValidators.notStartingOrEndingWithHyphen())
                .test("invalid_username")).isFalse();

        // Invalid case: starts with hyphen
        assertThat(GitHubUserNameValidators.notEmptyOrLengthOutOfBounds()
                .and(GitHubUserNameValidators.hasValidCharacters())
                .and(GitHubUserNameValidators.notStartingOrEndingWithHyphen())
                .test("-invalidUsername")).isFalse();

        // Invalid case: ends with hyphen
        assertThat(GitHubUserNameValidators.notEmptyOrLengthOutOfBounds()
                .and(GitHubUserNameValidators.hasValidCharacters())
                .and(GitHubUserNameValidators.notStartingOrEndingWithHyphen())
                .test("invalidUsername-")).isFalse();
    }
}
