package io.student.rcc.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import io.student.rcc.page.component.FormComponent;
import io.student.rcc.page.component.ItemCard;
import io.student.rcc.page.component.SearchField;
import io.student.rcc.page.component.Toast;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class MuseumPage {

    private final SelenideElement pageTitle = $("main h2");
    private final SelenideElement addMuseumButton = $(byText("Добавить музей"));
    private final SearchField searchField = new SearchField("Искать музей...");

    private final ElementsCollection museumCards = $$("a[href^='/museum/']");

    private final FormComponent museumForm = new FormComponent($("[data-testid='modal-component']"));
    private final Toast toast = new Toast($("[data-testid='toast']"));

    private final SelenideElement museumDetailsCard = $("main article.card");
    private final SelenideElement museumTitle = museumDetailsCard.$("header.card-header");
    private final SelenideElement museumLocation = museumTitle.sibling(0);
    private final SelenideElement editMuseumButton = museumDetailsCard.$("[data-testid='edit-museum']");
    private final SelenideElement museumDescriptionTextArea = editMuseumButton.closest("div").sibling(0);
    private final SelenideElement museumImage = museumDetailsCard.$("img");


    @Step("Проверка, что страница с музеями открыта")
    public MuseumPage checkMuseumPageIsOpened() {
        pageTitle.shouldHave(exactText("Музеи"));
        addMuseumButton.shouldBe(visible);
        searchField.shouldBeVisible();
        return this;
    }

    @Step("Проверка, что список с музеями не пустой")
    public MuseumPage checkMuseumListIsNotEmpty() {
        museumCards.shouldHave(sizeGreaterThanOrEqual(1));
        return this;
    }

    @Step("Нажать кнопку добавления музея")
    public MuseumPage clickAddMuseumButton() {
        addMuseumButton.click();
        museumForm.shouldBeOpened("");
        return this;
    }

    @Step("Найти музей с названием {museumName} через поиск")
    public MuseumPage searchMuseumBySearch(String museumName) {
        searchField.search(museumName);
        return this;
    }

    @Step("Проверка, что музей с названием {museumName} был найден")
    public void checkMuseumWasFound(String museumName) {
        museumCards.shouldHave(size(1));
        findMuseumCardByName(museumName).shouldBeVisible();
    }

    @Step("Кликнуть на музей с названием {museumName}")
    public MuseumPage clickMuseumByName(String museumName) {
        findMuseumCardByName(museumName).click();
        return this;
    }

    @Step("Ввести название музея: {museumName}")
    public MuseumPage setMuseumName(String museum) {
        museumForm.setInput("title", museum);
        return this;
    }

    @Step("Выбрать страну: {country}")
    public MuseumPage setCountry(String country) {
        museumForm.selectOption("countryId", country);
        return this;
    }

    @Step("Ввести город: {city}")
    public MuseumPage setCity(String city) {
        museumForm.setInput("city", city);
        return this;
    }

    @Step("Загрузить фотографию музея")
    public MuseumPage uploadMuseumPicture(@Nullable File picture) {
        if (picture != null) {
            museumForm.uploadFile("photo", picture);
        }
        return this;
    }

    @Step("Ввести описание")
    public MuseumPage setDescription(@Nullable String description) {
        museumForm.setTextarea("description", description);
        return this;
    }

    @Step("Сохранить новый музей")
    public MuseumPage clickAddButton() {
        museumForm.submit("Добавить");
        return this;
    }

    @Step("Проверка, что детальная информация о музее открыта")
    public void checkMuseumDetailsOpened(
        String title,
        String country,
        String city,
        @Nullable String description
    ) {
        museumDetailsCard.shouldBe(visible);
        museumTitle.shouldHave(exactText(title));
        museumLocation.shouldHave(exactText(country + ", " + city));

        if (description == null) {
            museumDescriptionTextArea.shouldBe(empty);
        } else {
            museumDescriptionTextArea.shouldHave(exactText(description));
        }
        museumImage.shouldBe(visible);
        editMuseumButton.shouldBe(visible);
    }

    @Step("Нажать на кнопку \"Редактировать\"")
    public MuseumPage clickEditButton() {
        editMuseumButton.click();
        return this;
    }

    @Step("Проверка, что окно редактирования музея открыто")
    public MuseumPage checkMuseumModalIsOpened() {
        museumForm.shouldBeOpened("Редактировать музей");
        return this;
    }

    @Step("Нажать на кнопку \"Сохранить\"")
    public MuseumPage clickSaveButton() {
        museumForm.submit("Сохранить");
        return this;
    }

    @Step("Проверка, что музей был добавлен в список")
    public void checkMuseumWasAddedToList(String title, String country, String city, @Nullable String description) {
        searchMuseumBySearch(title);
        findMuseumCardByName(title).shouldBeVisible();
        clickMuseumByName(title);
        checkMuseumDetailsOpened(title, country, city, description);
    }

    @Step("Проверка, что попап об обновлении музея показан")
    public MuseumPage checkToastIsDisplayed() {
        toast
            .shouldBeVisible()
            .shouldContainMessage("Обновлен музей");
        return this;
    }

    @Step("Нажать на кнопку \"Закрыть\" на попапе")
    public MuseumPage clickCloseToastButton() {
        toast.close();
        return this;
    }

    @Step("Проверка, что музей был отредактирован")
    public void checkMuseumWasChanged(String title, String country, String city, @Nullable String description) {
        checkMuseumDetailsOpened(title, country, city, description);
    }

    @Step("Проверка, что окно редактирования музея всё ещё открыто")
    public void checkMuseumModalFormIsStillOpened() {
        museumForm.shouldBeOpened();
    }

    private ItemCard findMuseumCardByName(String museumName) {
        return new ItemCard(
            museumCards.findBy(text(museumName))
        );
    }
}
