package io.student.rcc.tests;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import io.student.rcc.config.Config;
import io.student.rcc.page.MainPage;
import org.junit.jupiter.api.Test;

public class RegisterTests {

    private static final Config CFG = Config.getInstance();
    private final Faker faker = new Faker();
    private static final String VALID_PASSWORD = "12345";
    private static final String INVALID_PASSWORD = "123456";
    private static final String EXISTING_USERNAME = "duck";

    @Test
    void shouldRegisterNewUser() {
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .navigateToTheLoginPage()
                .navigateToTheRegisterPage()
                .setUsername(faker.name().username())
                .setPassword(VALID_PASSWORD)
                .setPasswordSubmit(VALID_PASSWORD)
                .submitRegistration()
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
                .submitRegistration()
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
                .submitRegistration()
                .checkErrorMessagePasswordsNotEqual();
    }
}
