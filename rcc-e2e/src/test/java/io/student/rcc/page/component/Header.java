package io.student.rcc.page.component;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class Header {

    private final SelenideElement self = $("#shell-header");
    private final SelenideElement paintingsLink = $("a[href='/painting']");
    private final SelenideElement artistsLink = $("a[href='/artist']");
    private final SelenideElement museumsLink = $("a[href='/museum']");

    public void checkHeaderText() {
        self.$("a[href='/']").shouldHave(text("Ro"));
        self.$("span.text_primary-500").shouldHave(text("coco"));
    }
}
