package io.student.rcc.page.component;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.*;

@ParametersAreNonnullByDefault
public class Toast extends BaseComponent{

    private final SelenideElement message;
    private final SelenideElement closeButton;

    public Toast(SelenideElement self) {
        super(self);
        this.message = self.$("div");
        this.closeButton = self.$("[aria-label='Dismiss toast']");
    }

    public Toast shouldBeVisible() {
        self.shouldBe(visible);
        return this;
    }

    public void shouldContainMessage(String text) {
        message.shouldHave(partialText(text));
    }

    public Toast close() {
        closeButton.click();
        self.should(disappear);
        return this;
    }
}
