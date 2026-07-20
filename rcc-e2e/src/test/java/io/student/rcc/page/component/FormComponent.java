package io.student.rcc.page.component;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;

@ParametersAreNonnullByDefault
public class FormComponent extends BaseComponent {

    private final SelenideElement header;
    private final SelenideElement form;
    private final SelenideElement closeButton;
    private final SelenideElement submitButton;

    public FormComponent(SelenideElement self) {
        super(self);

        this.header = self.$("header");
        this.form = self.$("form.modal-form");
        this.closeButton = form.$(byText("Закрыть"));
        this.submitButton = form.$("button[type='submit']");
    }

    public void shouldBeOpened(String expectedTitle) {
        self.shouldBe(visible);
        header.shouldHave(exactText(expectedTitle));
        form.shouldBe(visible);
    }

    public void shouldBeOpened() {
        self.shouldBe(visible);
        form.shouldBe(visible);
    }

    public void shouldHavePreviewImage() {
        form.$("img").shouldBe(visible);
    }

    public void setInput(String name, String value) {
        input(name).setValue(value);
    }

    public void selectOption(String name, String option) {
        select(name).selectOption(option);
    }

    public void setTextarea(String name, @Nullable String value) {
        textarea(name).setValue(value);
    }

    public void uploadFile(String name, File file) {
        input(name).uploadFile(file);
    }

    public void submit(String expectedButtonText) {
        submitButton
            .shouldBe(visible)
            .shouldHave(exactText(expectedButtonText))
            .click();

    }

    public FormComponent close() {
        closeButton
            .shouldBe(visible)
            .click();

        return this;
    }

    public SelenideElement input(String name) {
        return form.$("input[name='%s']"
            .formatted(name));
    }

    private SelenideElement select(String name) {
        return form.$("select[name='%s']"
            .formatted(name));
    }

    private SelenideElement textarea(String name) {
        return form.$("textarea[name='%s']"
            .formatted(name));
    }
}