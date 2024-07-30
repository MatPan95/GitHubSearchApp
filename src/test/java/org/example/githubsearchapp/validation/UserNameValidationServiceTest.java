package org.example.githubsearchapp.validation;

import org.example.githubsearchapp.Exceptions.InvalidUserNameException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class UserNameValidationServiceTest {

    private UserNameValidationService userNameValidationService;

    @BeforeEach
    void setUp() {
        userNameValidationService = new UserNameValidationService();
    }

    @Test
    void testValidateUser_withValidUsername() {
        // Given
        String validUsername = "valid-Username123";

        // When & Then (no exception should be thrown)
        userNameValidationService.validateUser(validUsername);
    }

    @Test
    void testValidateUser_withTooLongUsername() {
        // Given
        String tooLongUsername = "thisusernameiswaytoolongtobeacceptedbygithubsinceithasmorethan39characters";

        // When & Then
        assertThatThrownBy(() -> userNameValidationService.validateUser(tooLongUsername))
                .isInstanceOf(InvalidUserNameException.class);
    }

    @Test
    void testValidateUser_withInvalidCharacters() {
        // Given
        String invalidCharactersUsername = "invalid_username";

        // When & Then
        assertThatThrownBy(() -> userNameValidationService.validateUser(invalidCharactersUsername))
                .isInstanceOf(InvalidUserNameException.class);
    }

    @Test
    void testValidateUser_withUsernameStartingWithHyphen() {
        // Given
        String usernameStartingWithHyphen = "-username";

        // When & Then
        assertThatThrownBy(() -> userNameValidationService.validateUser(usernameStartingWithHyphen))
                .isInstanceOf(InvalidUserNameException.class);
    }

    @Test
    void testValidateUser_withUsernameEndingWithHyphen() {
        // Given
        String usernameEndingWithHyphen = "username-";

        // When & Then
        assertThatThrownBy(() -> userNameValidationService.validateUser(usernameEndingWithHyphen))
                .isInstanceOf(InvalidUserNameException.class);
    }
}
