package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.pages.DashboardPage;
import ru.netology.pages.PaymentPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreditGateTest {

    private DashboardPage dashboard;
    private PaymentPage creditPayment;

    @BeforeAll
    static void setUpListener() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownListener() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setUpApp() {
        dashboard = open("http://localhost:8080/", DashboardPage.class);
    }

    @AfterEach
    void cleanDataInDB() {
        SQLHelper.cleanAllData();
    }

    @Test
    @DisplayName("Успешная покупка тура с оплатой в кредит")
    void loanApprovedWithCreditGate() {
        creditPayment = dashboard.chooseCreditPayment();
        creditPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        creditPayment.enterDataAndContinue(card, month, year, cardholder, code);
        creditPayment.getSuccessMessage();
        var expected = DataHelper.getFirstCardStatus();
        var actual = SQLHelper.getRequestStatusCreditPayment();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Отправка формы с текущим годом в поле 'Год'")
    void loanApprovedWithPresentYear() {
        creditPayment = dashboard.chooseCreditPayment();
        creditPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getPresentYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        creditPayment.enterDataAndContinue(card, month, year, cardholder, code);
        creditPayment.getSuccessMessage();
        var expected = DataHelper.getFirstCardStatus();
        var actual = SQLHelper.getRequestStatusCreditPayment();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Отправка формы с максимально далеким годом в поле 'Год'")
    void loanDeclinedWithMaxYear() {
        creditPayment = dashboard.chooseCreditPayment();
        creditPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getMaximumTermInYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        creditPayment.enterDataAndContinue(card, month, year, cardholder, code);
        creditPayment.getSuccessMessage();
        var expected = DataHelper.getFirstCardStatus();
        var actual = SQLHelper.getRequestStatusCreditPayment();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Попытка покупки тура с оплатой в кредит по карте в статусе DECLINED")
    void loanDeclinedWithDeclinedCard() {
        creditPayment = dashboard.chooseCreditPayment();
        creditPayment.clearForm();
        var card = DataHelper.getSecondCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        creditPayment.enterDataAndContinue(card, month, year, cardholder, code);
        creditPayment.getRejectedMessage();
        var expected = DataHelper.getDeclinedCardStatus();
        var actual = SQLHelper.getRequestStatusCreditPayment();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Попытка покупки тура с оплатой в кредит по карте не из набора")
    void loanDeclinedWithCardNotFromSet() {
        creditPayment = dashboard.chooseCreditPayment();
        creditPayment.clearForm();
        var card = DataHelper.getRandomCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        creditPayment.enterDataAndContinue(card, month, year, cardholder, code);
        creditPayment.getRejectedMessage();
    }

    @Test
    @DisplayName("Отправка формы с пустыми полями")
    void loanDeclinedWithEmptyForm() {
        creditPayment = dashboard.chooseCreditPayment();
        creditPayment.clearForm();
        var card = DataHelper.getEmptyField();
        var month = DataHelper.getEmptyField();
        var year = DataHelper.getEmptyField();
        var cardholder = DataHelper.getEmptyField();
        var code = DataHelper.getEmptyField();
        creditPayment.enterDataAndContinue(card, month, year, cardholder, code);
        creditPayment.getEmptyFieldError();
    }
}
