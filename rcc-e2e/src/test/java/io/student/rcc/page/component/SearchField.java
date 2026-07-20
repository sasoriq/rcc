package io.student.rcc.page.component;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class SearchField {

    private final SelenideElement input;
    private final SelenideElement searchButton;

    public SearchField(String placeholder) {
        this.input = $("input[type='search'][placeholder='" + placeholder + "']");
        this.searchButton = input.parent().$("button");
    }

    public void shouldBeVisible() {
        input.shouldBe(visible);
        searchButton.shouldBe(visible);
    }

    public void search(String value) {
        input.setValue(value);
        searchButton.click();
    }
}
