package io.student.rcc.page.component;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.visible;

@ParametersAreNonnullByDefault
public class ItemCard extends BaseComponent {

    public ItemCard(SelenideElement self) {
        super(self);
    }

    public void shouldBeVisible() {
        self.shouldBe(visible);
        image().shouldBe(visible);
        title().shouldBe(visible);
    }

    public void click() {
        self.click();
    }

    private SelenideElement image() {
        return self.$("img");
    }

    private SelenideElement title() {
        return self.$("div");
    }
}