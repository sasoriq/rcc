package io.student.rcc.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {

    private final SelenideElement loginBtn = $(".btn");
    private final SelenideElement mainNav = $("nav[aria-label='Основная навигация']");
    private final SelenideElement addPaintingBtn = $("[href='/painting']");
    private final SelenideElement addArtistBtn = $("a[href='/artist']");
    private final SelenideElement addAMuseumBtn = $("a[href='/museum']");

    public LoginPage navigateToTheLoginPage() {
        loginBtn.click();
        return new LoginPage();
    }

    public void checkTheMainPageDisplayed() {
        mainNav.shouldBe(visible);
        mainNav.$("h1").shouldHave(text("Ваши любимые картины"));
        addPaintingBtn.shouldBe(visible);
        addArtistBtn.shouldBe(visible);
        addAMuseumBtn.shouldBe(visible);
    }
}
