package io.student.rcc.tests;

import com.codeborne.selenide.Selenide;
import io.student.rcc.config.Config;
import io.student.rcc.page.MainPage;
import org.junit.jupiter.api.Test;

import java.io.File;

public class ProfileTests {

    private static final Config CFG = Config.getInstance();
    private static final String USERNAME = "duck";
    private static final String FIRSTNAME = "Leonid";
    private static final String SURNAME = "Grach";
    private static final File PROFILE_IMG = new File("C:\\Users\\Viktoria\\OpenideProjects\\rcc2\\rcc-e2e\\src\\test\\resources\\images\\duck.jpg");

    @Test
    void editProfileData() {
        Selenide.open(CFG.frontUrl(), MainPage.class)
            .navigateToTheLoginPage()
            .setUsername("duck")
            .setPassword("12345")
            .successfulSubmitLogin()
            .navigateToTheProfilePage()
            .checkProfileModalIsOpened()
            .checkUsername(USERNAME)
            .uploadProfilePicture(PROFILE_IMG)
            .setFirstName(FIRSTNAME)
            .setSurname(SURNAME)
            .clickUpdateProfileButton()
            .navigateToTheProfilePage()
            .checkProfileDataUpdated(FIRSTNAME, SURNAME);
    }
}
