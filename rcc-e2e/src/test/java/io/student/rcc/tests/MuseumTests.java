package io.student.rcc.tests;

import com.codeborne.selenide.Selenide;
import io.student.rcc.config.Config;
import io.student.rcc.page.MainPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.UUID;

public class MuseumTests {

    private static final Config CFG = Config.getInstance();
    private static final String MUSEUM_NAME = "Städel Museum";
    private static final String COUNTRY = "Албания";
    private static final String TEST_COUNTRY = "Албания";
    private static final String CITY = "Франкфурт";
    private static final String TEST_CITY = "Сидней";
    private static final String DESCRIPTION = "Тестовый музей";
    private static final String TEST_DESCRIPTION = "Обновленный тестовый музей";
    private static final File PICTURE = new File("src\\test\\resources\\images\\cat.jpg");
    private static final File PICTURE_D = new File("src\\test\\resources\\images\\duck.jpg");

    @BeforeEach
    void setup() {
        Selenide.open(CFG.frontUrl(), MainPage.class)
            .navigateToTheLoginPage()
            .setUsername("duck")
            .setPassword("12345")
            .successfulSubmitLogin()
            .checkTheMainPageDisplayed();
    }

    @Test
    void findMuseumBySearchField() {
        Selenide.open(CFG.frontUrl(), MainPage.class)
            .navigateToTheMuseumsPage()
            .checkMuseumPageIsOpened()
            .checkMuseumListIsNotEmpty()
            .searchMuseumBySearch(MUSEUM_NAME)
            .checkMuseumWasFound(MUSEUM_NAME);
    }

    @Test
    void openMuseumByName() {
        Selenide.open(CFG.frontUrl(), MainPage.class)
            .navigateToTheMuseumsPage()
            .checkMuseumPageIsOpened()
            .clickMuseumByName(MUSEUM_NAME)
            .checkMuseumDetailsOpened(MUSEUM_NAME, COUNTRY, CITY, DESCRIPTION);
    }

    @Test
    void editMuseum() {
        String museumName = "Museum " + UUID.randomUUID().toString().substring(0, 3);
        Selenide.open(CFG.frontUrl(), MainPage.class)
            .navigateToTheMuseumsPage()
            .checkMuseumPageIsOpened()
            .clickMuseumByName(MUSEUM_NAME)
            .clickEditButton()
            .checkMuseumModalIsOpened()
            .uploadMuseumPicture(PICTURE_D)
            .setMuseumName(museumName)
            .setCountry(TEST_COUNTRY)
            .setCity(TEST_CITY)
            .setDescription(TEST_DESCRIPTION)
            .clickSaveButton()
            .checkToastIsDisplayed()
            .clickCloseToastButton()
            .checkMuseumWasChanged(museumName, TEST_COUNTRY, TEST_CITY, TEST_DESCRIPTION);
    }

    @Test
    void addMuseum() {
        String museumName = "Museum " + UUID.randomUUID().toString().substring(0, 3);
        Selenide.open(CFG.frontUrl(), MainPage.class)
            .navigateToTheMuseumsPage()
            .checkMuseumPageIsOpened()
            .clickAddMuseumButton()
            .uploadMuseumPicture(PICTURE)
            .setMuseumName(museumName)
            .setCountry(COUNTRY)
            .setCity(TEST_CITY)
            .setDescription(TEST_DESCRIPTION)
            .clickAddButton()
            .checkMuseumWasAddedToList(museumName, COUNTRY, TEST_CITY, TEST_DESCRIPTION);
    }

    @Test
    void shouldAddMuseumWithMinimumTitleLength() {
        String title = "А";

        Selenide.open(CFG.frontUrl(), MainPage.class)
            .navigateToTheMuseumsPage()
            .checkMuseumPageIsOpened()
            .clickAddMuseumButton()
            .uploadMuseumPicture(PICTURE)
            .setMuseumName(title)
            .setCountry(COUNTRY)
            .setCity(TEST_CITY)
            .setDescription(TEST_DESCRIPTION)
            .clickAddButton()
            .checkMuseumWasAddedToList(title, COUNTRY, TEST_CITY, TEST_DESCRIPTION);
    }

    @Test
    void shouldNotAddMuseumWithEmptyTitle() {
        Selenide.open(CFG.frontUrl(), MainPage.class)
            .navigateToTheMuseumsPage()
            .checkMuseumPageIsOpened()
            .clickAddMuseumButton()
            .uploadMuseumPicture(PICTURE)
            .setMuseumName("")
            .setCountry("Австралия")
            .setCity(TEST_CITY)
            .setDescription(TEST_DESCRIPTION)
            .clickAddButton()
            .checkMuseumModalFormIsStillOpened();
    }
}
