package io.student.rcc.tests;

import com.codeborne.selenide.Selenide;
import io.student.rcc.config.Config;
import io.student.rcc.page.MainPage;
import org.junit.jupiter.api.Test;

public class LoginTests {

    private static final Config CFG = Config.getInstance();
    private static final String INVALID_PASSWORD = "123456";
    private static final String INVALID_USERNAME = "username";

    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .navigateToTheLoginPage()
                .setUsername("duck")
                .setPassword("12345")
                .successfulSubmitLogin()
                .checkTheMainPageDisplayed();
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .navigateToTheLoginPage()
                .setUsername(INVALID_USERNAME)
                .setPassword(INVALID_PASSWORD)
                .unsuccessfulSubmitLogin()
                .checkErrorMessageIncorrectCredentials()
                .checkStillStayOnLoginPage();
    }
}
