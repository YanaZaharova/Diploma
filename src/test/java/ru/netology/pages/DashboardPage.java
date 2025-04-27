package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class DashboardPage {

    private final SelenideElement heading = $("[class='heading heading_size_l heading_theme_alfa-on-white']");
    private final SelenideElement cardPaymentButton = $("[class='button button_size_m button_theme_alfa-on-white']");
    private final SelenideElement creditPaymentButton = $("[class='button button_view_extra button_size_m button_theme_alfa-on-white']");
    private final SelenideElement cardPaymentHeading = $(byText("Оплата по карте"));
    private final SelenideElement creditPaymentHeading = $(byText("Кредит по данным карты"));

    public DashboardPage() {
        heading.shouldBe(visible);
    }

    public void chooseCardPayment() {
        cardPaymentButton.shouldHave(exactText("Купить")).click();
        cardPaymentHeading.shouldBe(visible);
    }

    public void chooseCreditPayment() {
        creditPaymentButton.shouldHave(exactText("Купить в кредит")).click();
        creditPaymentHeading.shouldBe(visible);
    }
}
