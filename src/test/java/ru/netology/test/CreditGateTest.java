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
    @DisplayName("Отправка формы с текущим годом и текущим месяцем")
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
    @DisplayName("Отправка формы с максимально далеким годом и текущим месяцем")
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

    @Test
    @DisplayName("Отправка формы с номером карты из 15-ти символов")
    void loanDeclinedWith15SymbolsCard() {
        creditPayment = dashboard.chooseCreditPayment();
        creditPayment.clearForm();
        var card = DataHelper.get15SymbolCard();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        creditPayment.enterDataAndContinue(card, month, year, cardholder, code);
        creditPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Отправка формы с номером карты из 1-го символа")
    void loanDeclinedWithOneSymbolCard() {
        creditPayment = dashboard.chooseCreditPayment();
        creditPayment.clearForm();
        var card = DataHelper.getOneSymbolCard();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        creditPayment.enterDataAndContinue(card, month, year, cardholder, code);
        creditPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Отправка формы с пустым полем для номера карты")
    void loanDeclinedWithEmptyCard() {
        creditPayment = dashboard.chooseCreditPayment();
        creditPayment.clearForm();
        var card = DataHelper.getEmptyField();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        creditPayment.enterDataAndContinue(card, month, year, cardholder, code);
        creditPayment.getEmptyFieldError();
    }

    @Test
    @DisplayName("Отправка формы с кириллицей в поле Номер карты")
    void loanDeclinedWithCyrillicLettersInCard() {
        creditPayment = dashboard.chooseCreditPayment();
        creditPayment.clearForm();
        var card = DataHelper.getRusTextInCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        creditPayment.enterDataAndContinue(card, month, year, cardholder, code);
        creditPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Отправка формы с латиницей в поле Номер карты")
    void loanDeclinedWithEnglishLettersInCard() {
        creditPayment = dashboard.chooseCreditPayment();
        creditPayment.clearForm();
        var card = DataHelper.getEngTextInCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        creditPayment.enterDataAndContinue(card, month, year, cardholder, code);
        creditPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Отправка формы со специальными символами в поле Номер карты")
    void loanDeclinedWithDiacriticsInCard() {
        creditPayment = dashboard.chooseCreditPayment();
        creditPayment.clearForm();
        var card = DataHelper.getDiacriticsInField();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        creditPayment.enterDataAndContinue(card, month, year, cardholder, code);
        creditPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Отправка формы с одним символом в поле “Месяц”")
    void loanDeclinedWithOneSymbolMonth() {
        creditPayment = dashboard.chooseCreditPayment();
        creditPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getOneSymbolInMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        creditPayment.enterDataAndContinue(card, month, year, cardholder, code);
        creditPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Отправка формы с номером выше 12-го в поле “Месяц”")
    void loanDeclinedWithNonexistentMonth() {
        creditPayment = dashboard.chooseCreditPayment();
        creditPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.get13InMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        creditPayment.enterDataAndContinue(card, month, year, cardholder, code);
        creditPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Отправка формы с нулями в поле 'Месяц'")
    void loanDeclinedWithZerosInMonth() {
        creditPayment = dashboard.chooseCreditPayment();
        creditPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getZerosInMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        creditPayment.enterDataAndContinue(card, month, year, cardholder, code);
        creditPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Отправка формы с пустым полем “Месяц”")
    void loanDeclinedWithEmptyMonth() {
        creditPayment = dashboard.chooseCreditPayment();
        creditPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getEmptyField();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        creditPayment.enterDataAndContinue(card, month, year, cardholder, code);
        creditPayment.getEmptyFieldError();
    }

    @Test
    @DisplayName("Отправка формы с кириллицей в поле “Месяц”")
    void loanDeclinedWithCyrillicLettersInMonth() {
        creditPayment = dashboard.chooseCreditPayment();
        creditPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getRusTextInMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        creditPayment.enterDataAndContinue(card, month, year, cardholder, code);
        creditPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Отправка формы с латиницей в поле “Месяц”")
    void loanDeclinedWithEnglishLettersInMonth() {
        creditPayment = dashboard.chooseCreditPayment();
        creditPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getEngTextInMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        creditPayment.enterDataAndContinue(card, month, year, cardholder, code);
        creditPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Отправка формы со специальными символами в поле “Месяц”")
    void loanDeclinedWithDiacriticsInMonth() {
        creditPayment = dashboard.chooseCreditPayment();
        creditPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getDiacriticsInField();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        creditPayment.enterDataAndContinue(card, month, year, cardholder, code);
        creditPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Отправка формы с предыдущим месяцем и текущим годом")
    void loanDeclinedWithPreviousMonthAndPresentYear() {
        creditPayment = dashboard.chooseCreditPayment();
        creditPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getPreviousMonth();
        var year = DataHelper.getPresentYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        creditPayment.enterDataAndContinue(card, month, year, cardholder, code);
        creditPayment.getWrongYearError();
    }

    @Test
    @DisplayName("Отправка формы с предыдущим годом")
    void loanDeclinedWithPreviousYear() {
        creditPayment = dashboard.chooseCreditPayment();
        creditPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getPreviousYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        creditPayment.enterDataAndContinue(card, month, year, cardholder, code);
        creditPayment.getWrongYearError();
    }

    @Test
    @DisplayName("Отправка формы с одним символом в поле “Год”")
    void loanDeclinedWithOneSymbolYear() {
        creditPayment = dashboard.chooseCreditPayment();
        creditPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getOneSymbolInYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        creditPayment.enterDataAndContinue(card, month, year, cardholder, code);
        creditPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Отправка формы с неправдоподобно большим сроком карты(на месяц больше 10-ти лет)")
    void loanDeclinedWithMoreThen10YearsTerm() {
        creditPayment = dashboard.chooseCreditPayment();
        creditPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getNextMonth();
        var year = DataHelper.getMaximumTermInYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        creditPayment.enterDataAndContinue(card, month, year, cardholder, code);
        creditPayment.getWrongMonthError();
    }

    @Test
    @DisplayName("Отправка формы с пустым полем “Год”")
    void loanDeclinedWithEmptyYear() {
        creditPayment = dashboard.chooseCreditPayment();
        creditPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getEmptyField();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        creditPayment.enterDataAndContinue(card, month, year, cardholder, code);
        creditPayment.getEmptyFieldError();
    }

    @Test
    @DisplayName("Отправка формы с кириллицей в поле “Год”")
    void loanDeclinedWithCyrillicLettersInYear() {
        creditPayment = dashboard.chooseCreditPayment();
        creditPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getRusTextInYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        creditPayment.enterDataAndContinue(card, month, year, cardholder, code);
        creditPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Отправка формы с латиницей в поле “Год”")
    void loanDeclinedWithEnglishLettersInYear() {
        creditPayment = dashboard.chooseCreditPayment();
        creditPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getEngTextInYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        creditPayment.enterDataAndContinue(card, month, year, cardholder, code);
        creditPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Отправка формы со спец. символами в поле “Год”")
    void loanDeclinedWithDiacriticsInYear() {
        creditPayment = dashboard.chooseCreditPayment();
        creditPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getDiacriticsInField();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        creditPayment.enterDataAndContinue(card, month, year, cardholder, code);
        creditPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Ввести данные с использованием кириллицы в поле “Владелец”")
    void loanDeclinedWithCyrillicLettersInOwner() {
        creditPayment = dashboard.chooseCreditPayment();
        creditPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getRussianKeyboardNameInOwner();
        var code = DataHelper.getValidCode();
        creditPayment.enterDataAndContinue(card, month, year, cardholder, code);
        creditPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Ввести данные с использованием чисел в поле “Владелец”")
    void loanDeclinedWithNumbersInOwner() {
        creditPayment = dashboard.chooseCreditPayment();
        creditPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getNumbersInOwner();
        var code = DataHelper.getValidCode();
        creditPayment.enterDataAndContinue(card, month, year, String.valueOf(cardholder), code);
        creditPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Ввести данные с использованием специальных символов в поле “Владелец”")
    void loanDeclinedWithDiacriticsInOwner() {
        creditPayment = dashboard.chooseCreditPayment();
        creditPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getDiacriticsInField();
        var code = DataHelper.getValidCode();
        creditPayment.enterDataAndContinue(card, month, year, cardholder, code);
        creditPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Ввести несколько пробелов в поле “Владелец”")
    void loanDeclinedWithSpacesInOwner() {
        creditPayment = dashboard.chooseCreditPayment();
        creditPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getSpacesInField();
        var code = DataHelper.getValidCode();
        creditPayment.enterDataAndContinue(card, month, year, cardholder, code);
        creditPayment.getEmptyFieldError();
    }

    @Test
    @DisplayName("Ввести два символа в поле “CVC/CVV”")
    void loanDeclinedWithTwoSymbolsInCode() {
        creditPayment = dashboard.chooseCreditPayment();
        creditPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getCodeWithTwoNumbers();
        creditPayment.enterDataAndContinue(card, month, year, cardholder, code);
        creditPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Отправка формы с пустым полем “CVC/CVV”")
    void loanDeclinedWithEmptyCode() {
        creditPayment = dashboard.chooseCreditPayment();
        creditPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getEmptyField();
        creditPayment.enterDataAndContinue(card, month, year, cardholder, code);
        creditPayment.getEmptyFieldError();
    }

    @Test
    @DisplayName("Ввести кириллицу в поле “CVC/CVV”")
    void loanDeclinedWithCyrillicLettersInCode() {
        creditPayment = dashboard.chooseCreditPayment();
        creditPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getRusTextInCode();
        creditPayment.enterDataAndContinue(card, month, year, cardholder, code);
        creditPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Ввести латиницу в поле “CVC/CVV”")
    void loanDeclinedWithEnglishLettersInCode() {
        creditPayment = dashboard.chooseCreditPayment();
        creditPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getEngTextInCode();
        creditPayment.enterDataAndContinue(card, month, year, cardholder, code);
        creditPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Ввести спец. символы в поле “CVC/CVV”")
    void loanDeclinedWithDiacriticsInCode() {
        creditPayment = dashboard.chooseCreditPayment();
        creditPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getDiacriticsInField();
        creditPayment.enterDataAndContinue(card, month, year, cardholder, code);
        creditPayment.getWrongFormatError();
    }
}
