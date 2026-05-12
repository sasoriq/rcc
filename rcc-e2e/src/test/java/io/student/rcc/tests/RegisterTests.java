package io.student.rcc.tests;

import com.codeborne.selenide.Selenide;
import io.student.rcc.config.Config;
import io.student.rcc.page.MainPage;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class RegisterTests {
    private static final Config CFG = Config.getInstance();
    private static final String VALID_PASSWORD = "12345";
    private static final String INVALID_PASSWORD = "123456";
    private static final String EXISTING_USERNAME = "duck";

    @Test
    void shouldRegisterNewUser() {
        var newUsername = "user" + UUID.randomUUID();
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .navigateToTheLoginPage()
                .navigateToTheRegisterPage()
                .setUsername(newUsername)
                .setPassword(VALID_PASSWORD)
                .setPasswordSubmit(VALID_PASSWORD)
                .submitRegisration()
                .checkRegistrationWasSuccessful()
                .navigateToTheMainPageAfterClickSignIn();
    }

    @Test
    void shouldNotRegisterNewUserWithExistingUsername() {
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .navigateToTheLoginPage()
                .navigateToTheRegisterPage()
                .setUsername(EXISTING_USERNAME)
                .setPassword(VALID_PASSWORD)
                .setPasswordSubmit(VALID_PASSWORD)
                .submitRegisration()
                .checkErrorMessageUsernameAlreadyExists(EXISTING_USERNAME);
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .navigateToTheLoginPage()
                .navigateToTheRegisterPage()
                .setUsername(EXISTING_USERNAME)
                .setPassword(VALID_PASSWORD)
                .setPasswordSubmit(INVALID_PASSWORD)
                .submitRegisration()
                .checkErrorMessagePasswordsNotEqual();
    }
}
