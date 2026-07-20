package io.student.rcc.page;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class MainPage {

    private final SelenideElement loginBtn = $(".btn");
    private final SelenideElement profileBtn = $("[data-testid='avatar']").closest("button");
    private final SelenideElement mainNav = $("nav[aria-label='Основная навигация']");
    private final SelenideElement addPaintingBtn = $("[href='/painting']");
    private final SelenideElement addArtistBtn = $("a[href='/artist']");
    private final SelenideElement addMuseumBtn = $("a[href='/museum']");

    public LoginPage navigateToTheLoginPage() {
        loginBtn.click();
        return new LoginPage();
    }

    public void checkTheMainPageDisplayed() {
        mainNav.shouldBe(visible);
        mainNav.$("h1").shouldHave(text("Ваши любимые картины"));
        addPaintingBtn.shouldBe(visible);
        addArtistBtn.shouldBe(visible);
        addMuseumBtn.shouldBe(visible);
    }

    public MainPage loginToTheAccount() {
        navigateToTheLoginPage()
            .setUsername("duck")
            .setPassword("12345")
            .successfulSubmitLogin()
            .checkTheMainPageDisplayed();
        return this;
    }

    public PaintingPage navigateToThePaintingsPage() {
        addPaintingBtn.click();
        return new PaintingPage();
    }

    public ArtistPage navigateToTheArtistsPage() {
        addArtistBtn.click();
        return new ArtistPage();
    }

    public MuseumPage navigateToTheMuseumsPage() {
        addMuseumBtn.click();
        return new MuseumPage();
    }

    public ProfilePage navigateToTheProfilePage() {
        profileBtn.click();
        return new ProfilePage();
    }
}
