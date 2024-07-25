package org.example.githubsearchapp.validation;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserNameValidationService.class})
@ExtendWith(SpringExtension.class)
class UserNameValidationServiceDiffblueTest {
    @Autowired
    private UserNameValidationService userNameValidationService;

    /**
     * Method under test: {@link UserNameValidationService#validateUser(String)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testValidateUser() {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Reason: No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   org.example.githubsearchapp.Exceptions.InvalidUserNameException
        //       at java.base/java.util.Optional.orElseThrow(Optional.java:403)
        //       at org.example.githubsearchapp.validation.UserNameValidationService.validateUser(UserNameValidationService.java:18)
        //   See https://diff.blue/R013 to resolve this issue.

        // Arrange and Act
        userNameValidationService.validateUser("^[a-zA-Z0-9-]+$");
    }

    /**
     * Method under test: {@link UserNameValidationService#validateUser(String)}
     */
    @Test
    void testValidateUser2() {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Diffblue AI was unable to find a test

        // Arrange and Act
        userNameValidationService.validateUser("janedoe");
    }
}
