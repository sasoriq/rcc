package io.student.rcc.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import io.student.rcc.page.component.FormComponent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class ProfilePage {

    private final SelenideElement modalComponent = $("[data-testid='modal-component']");

    private final FormComponent profileForm = new FormComponent(modalComponent);

    private final SelenideElement username = modalComponent.$("h4");
    private final SelenideElement logoutButton = modalComponent.$(byText("Выйти"));

    @Step("Проверка, что страница профиля открыта")
    public ProfilePage checkProfileModalIsOpened() {
        modalComponent.shouldBe(visible);
        profileForm.shouldBeOpened("Профиль");
        return this;
    }

    @Step("Проверка, что username совпадает с ожидаемым")
    public ProfilePage checkUsername(String username) {
        this.username.shouldHave(exactText("@" + username));
        return this;
    }

    @Step("Загрузить аватар профиля")
    public ProfilePage uploadProfilePicture(File picture) {
        profileForm.uploadFile("content", picture);
        return this;
    }

    @Step("Ввести имя пользователя {firstName}")
    public ProfilePage setFirstName(String firstName) {
        profileForm.setInput("firstname", firstName);
        return this;
    }

    @Step("Ввести фамилию пользователя {surname}")
    public ProfilePage setSurname(String surname) {
        profileForm.setInput("surname", surname);
        return this;
    }

    @Step("Нажать на кнопку \"Обновить профиль\"")
    public MainPage clickUpdateProfileButton() {
        profileForm.submit("Обновить профиль");
        return new MainPage();
    }

    @Step("Проверка, что данные в профиле были обновлены")
    public void checkProfileDataUpdated(String firstName, String surname) {
        profileForm.input("firstname").shouldHave(value(firstName));
        profileForm.input("surname").shouldHave(value(surname));
    }

    @Step("Нажать на кнопку \"Закрыть\"")
    public ProfilePage clickCloseButton() {
        profileForm.close();
        return this;
    }

    @Step("Нажать на кнопку \"Выйти\"")
    public ProfilePage clickLogoutButton() {
        logoutButton.shouldBe(visible).click();
        return this;
    }
}
