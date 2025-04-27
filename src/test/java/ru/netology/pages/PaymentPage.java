package ru.netology.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PaymentPage {

    private final SelenideElement cardField = $("[placeholder='0000 0000 0000 0000']");
    private final SelenideElement monthField = $("[placeholder='08']");
    private final SelenideElement yearField = $("[placeholder='22']");
    private final SelenideElement cardholderField = $$("[class='input__box']").get(3);
    private final SelenideElement codeField = $("[placeholder='999']");

    private final SelenideElement sendAnApplicationButton = $(byText("Продолжить"));

    private final SelenideElement successMessage = $(".notification_status_ok");
    private final SelenideElement rejectedMessage = $(".notification_status_error");

    private final SelenideElement wrongFormatError = $(byText("Неверный формат"));
    private final SelenideElement emptyFieldError = $(byText("Поле обязательно для заполнения"));
    private final SelenideElement wrongMonthError = $(byText("Неверно указан срок действия карты"));
    private final SelenideElement wrongYearError = $(byText("Истёк срок действия карты"));
    private final SelenideElement wrongSymbolsInCardholder = $(byText("Цифры и специальные символы недопустимы"));

    public PaymentPage clearForm() {
        clearAllFields();
        return new PaymentPage();
    }

    public void clearAllFields() {
        cardField.doubleClick().sendKeys(Keys.BACK_SPACE);
        monthField.doubleClick().sendKeys(Keys.BACK_SPACE);
        yearField.doubleClick().sendKeys(Keys.BACK_SPACE);
        cardholderField.doubleClick().sendKeys(Keys.BACK_SPACE);
        codeField.doubleClick().sendKeys(Keys.BACK_SPACE);
    }

    public void enterDataAndContinue(String card, String month, String year, String cardholder, String code) {
        cardField.sendKeys(card);
        monthField.sendKeys(month);
        yearField.sendKeys(year);
        cardholderField.sendKeys(cardholder);
        codeField.sendKeys(code);
        sendAnApplicationButton.click();
    }

    public void getSuccessMessage() {
        successMessage.shouldBe(visible, Duration.ofSeconds(10)).shouldHave(Condition.text("Успешно"));
    }

    public void getRejectedMessage() {
        rejectedMessage.shouldBe(visible, Duration.ofSeconds(10)).shouldHave(Condition.text("Ошибка"));
    }

    public void getWrongFormatError() {
        wrongFormatError.shouldBe(visible);
    }

    public void getEmptyFieldError() {
        emptyFieldError.shouldBe(visible);
    }

    public void getWrongMonthError() {
        wrongMonthError.shouldBe(visible);
    }

    public void getWrongYearError() {
        wrongYearError.shouldBe(visible);
    }

    public void getWrongSymbolsInCardholder() {
        wrongSymbolsInCardholder.shouldBe(visible);
    }
}
