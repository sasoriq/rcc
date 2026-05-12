package io.student.rcc.tests;

import com.codeborne.selenide.Selenide;
import io.student.rcc.config.Config;
import io.student.rcc.jupiter.annotation.User;
import io.student.rcc.model.UserJson;
import io.student.rcc.page.MainPage;
import org.junit.jupiter.api.Test;

public class LoginTests {

    private static final Config CFG = Config.getInstance();
    private static final String INVALID_PASSWORD = "123456";
    private static final String INVALID_USERNAME = "username";

    @User
    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin(UserJson user) {
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .navigateToTheLoginPage()
                .setUsername(user.username())
                .setPassword(user.password())
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
