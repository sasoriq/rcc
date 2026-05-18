package io.student.rcc.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement confirmPasswordInput = $("#passwordSubmit");
    private final SelenideElement submitBtn = $(".form__submit");
    private final SelenideElement subHeader = $(".form__subheader");
    private final SelenideElement loginBtn = $(".form__submit");
    private final SelenideElement contentImg = $(".content__image");

    private final SelenideElement passwordErrorMessage = $(".form__error.error__password");
    private final SelenideElement usernameErrorMessage = $(".form__error.error__username");


    public RegisterPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    public RegisterPage setPasswordSubmit(String confirmPassword) {
        confirmPasswordInput.setValue(confirmPassword);
        return this;
    }

    public RegisterPage submitRegistration() {
        submitBtn.click();
        return this;
    }

    public RegisterPage checkRegistrationWasSuccessful() {
        subHeader.shouldBe(visible).shouldHave(text("Добро пожаловать в Rococo"));
        loginBtn.shouldBe(visible).shouldHave(text("Войти в систему"));
        contentImg.shouldBe(visible);
        return this;
    }

    public void navigateToTheMainPageAfterClickSignIn() {
        loginBtn.click();
    }

    public void checkErrorMessagePasswordsNotEqual() {
        passwordErrorMessage.shouldBe(visible).shouldHave(text("Passwords should be equal"));
    }

    public void checkErrorMessageUsernameAlreadyExists(String username) {
        usernameErrorMessage.shouldBe(visible).shouldHave(text("Username `" + username + "` already exists"));
    }
}
