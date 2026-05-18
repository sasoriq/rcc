package io.student.rcc.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement usernameInput = $("[name='username']");
    private final SelenideElement passwordInput = $("[name='password']");
    private final SelenideElement submitBtn = $(".form__submit");
    private final SelenideElement registerBtn = $(".form__link");
    private final SelenideElement credentialsErrorMessage = $(".form__error.login__error");

    public LoginPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    public LoginPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    public MainPage successfulSubmitLogin() {
        submitBtn.click();
        return new MainPage();
    }

    public LoginPage unsuccessfulSubmitLogin() {
        submitBtn.click();
        return this;
    }

    public RegisterPage navigateToTheRegisterPage() {
        registerBtn.click();
        return new RegisterPage();
    }

    public LoginPage checkErrorMessageIncorrectCredentials() {
        credentialsErrorMessage.shouldBe(visible).shouldHave(text("Неверные учетные данные пользователя"));
        return this;
    }

    public void checkStillStayOnLoginPage() {
        usernameInput.shouldBe(visible);
        passwordInput.shouldBe(visible);
        submitBtn.shouldBe(visible);
    }
}
