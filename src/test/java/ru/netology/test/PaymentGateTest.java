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

public class PaymentGateTest {

    private DashboardPage dashboard;
    private PaymentPage cardPayment;

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
    @DisplayName("Успешная покупка тура с оплатой по карте")
    void successfulPurchaseWithPaymentGate() {
        cardPayment = dashboard.chooseCardPayment();
        cardPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        cardPayment.enterDataAndContinue(card, month, year, cardholder, code);
        cardPayment.getSuccessMessage();
        var expected = DataHelper.getFirstCardStatus();
        var actual = SQLHelper.getRequestStatusCardPayment();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Отправка формы с текущим годом и текущим месяцем")
    void successfulPurchaseWithPresentYear() {
        cardPayment = dashboard.chooseCardPayment();
        cardPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getPresentYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        cardPayment.enterDataAndContinue(card, month, year, cardholder, code);
        cardPayment.getSuccessMessage();
        var expected = DataHelper.getFirstCardStatus();
        var actual = SQLHelper.getRequestStatusCardPayment();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Отправка формы с максимально далеким годом и текущим месяцем")
    void successfulPurchaseWithMaxYear() {
        cardPayment = dashboard.chooseCardPayment();
        cardPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getMaximumTermInYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        cardPayment.enterDataAndContinue(card, month, year, cardholder, code);
        cardPayment.getSuccessMessage();
        var expected = DataHelper.getFirstCardStatus();
        var actual = SQLHelper.getRequestStatusCardPayment();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Попытка покупки тура с оплатой по карте в статусе DECLINED")
    void declinePurchaseWithDeclinedCard() {
        cardPayment = dashboard.chooseCardPayment();
        cardPayment.clearForm();
        var card = DataHelper.getSecondCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        cardPayment.enterDataAndContinue(card, month, year, cardholder, code);
        cardPayment.getRejectedMessage();
        var expected = DataHelper.getDeclinedCardStatus();
        var actual = SQLHelper.getRequestStatusCardPayment();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Попытка покупки тура с оплатой по карте не из набора")
    void declinePurchaseWithCardNotFromSet() {
        cardPayment = dashboard.chooseCardPayment();
        cardPayment.clearForm();
        var card = DataHelper.getRandomCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        cardPayment.enterDataAndContinue(card, month, year, cardholder, code);
        cardPayment.getRejectedMessage();
    }

    @Test
    @DisplayName("Отправка формы с пустыми полями")
    void declinePurchaseWithEmptyForm() {
        cardPayment = dashboard.chooseCardPayment();
        cardPayment.clearForm();
        var card = DataHelper.getEmptyField();
        var month = DataHelper.getEmptyField();
        var year = DataHelper.getEmptyField();
        var cardholder = DataHelper.getEmptyField();
        var code = DataHelper.getEmptyField();
        cardPayment.enterDataAndContinue(card, month, year, cardholder, code);
        cardPayment.getEmptyFieldError();
    }

    @Test
    @DisplayName("Отправка формы с номером карты из 15-ти символов")
    void declinePurchaseWith15SymbolsCard() {
        cardPayment = dashboard.chooseCardPayment();
        cardPayment.clearForm();
        var card = DataHelper.get15SymbolCard();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        cardPayment.enterDataAndContinue(card, month, year, cardholder, code);
        cardPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Отправка формы с номером карты из 1-го символа")
    void declinePurchaseWithOneSymbolCard() {
        cardPayment = dashboard.chooseCardPayment();
        cardPayment.clearForm();
        var card = DataHelper.getOneSymbolCard();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        cardPayment.enterDataAndContinue(card, month, year, cardholder, code);
        cardPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Отправка формы с пустым полем для номера карты")
    void declinePurchaseWithEmptyCard() {
        cardPayment = dashboard.chooseCardPayment();
        cardPayment.clearForm();
        var card = DataHelper.getEmptyField();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        cardPayment.enterDataAndContinue(card, month, year, cardholder, code);
        cardPayment.getEmptyFieldError();
    }

    @Test
    @DisplayName("Отправка формы с кириллицей в поле Номер карты")
    void declinePurchaseWithCyrillicLettersInCard() {
        cardPayment = dashboard.chooseCardPayment();
        cardPayment.clearForm();
        var card = DataHelper.getRusTextInCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        cardPayment.enterDataAndContinue(card, month, year, cardholder, code);
        cardPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Отправка формы с латиницей в поле Номер карты")
    void declinePurchaseWithEnglishLettersInCard() {
        cardPayment = dashboard.chooseCardPayment();
        cardPayment.clearForm();
        var card = DataHelper.getEngTextInCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        cardPayment.enterDataAndContinue(card, month, year, cardholder, code);
        cardPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Отправка формы со специальными символами в поле Номер карты")
    void declinePurchaseWithDiacriticsInCard() {
        cardPayment = dashboard.chooseCardPayment();
        cardPayment.clearForm();
        var card = DataHelper.getDiacriticsInField();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        cardPayment.enterDataAndContinue(card, month, year, cardholder, code);
        cardPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Отправка формы с одним символом в поле “Месяц”")
    void declinePurchaseWithOneSymbolMonth() {
        cardPayment = dashboard.chooseCardPayment();
        cardPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getOneSymbolInMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        cardPayment.enterDataAndContinue(card, month, year, cardholder, code);
        cardPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Отправка формы с номером выше 12-го в поле “Месяц”")
    void declinePurchaseWithNonexistentMonth() {
        cardPayment = dashboard.chooseCardPayment();
        cardPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.get13InMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        cardPayment.enterDataAndContinue(card, month, year, cardholder, code);
        cardPayment.getWrongMonthError();
    }

    @Test
    @DisplayName("Отправка формы с нулями в поле 'Месяц'")
    void declinePurchaseWithZerosInMonth() {
        cardPayment = dashboard.chooseCardPayment();
        cardPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getZerosInMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        cardPayment.enterDataAndContinue(card, month, year, cardholder, code);
        cardPayment.getWrongMonthError();
    }

    @Test
    @DisplayName("Отправка формы с пустым полем “Месяц”")
    void declinePurchaseWithEmptyMonth() {
        cardPayment = dashboard.chooseCardPayment();
        cardPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getEmptyField();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        cardPayment.enterDataAndContinue(card, month, year, cardholder, code);
        cardPayment.getEmptyFieldError();
    }

    @Test
    @DisplayName("Отправка формы с кириллицей в поле “Месяц”")
    void declinePurchaseWithCyrillicLettersInMonth() {
        cardPayment = dashboard.chooseCardPayment();
        cardPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getRusTextInMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        cardPayment.enterDataAndContinue(card, month, year, cardholder, code);
        cardPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Отправка формы с латиницей в поле “Месяц”")
    void declinePurchaseWithEnglishLettersInMonth() {
        cardPayment = dashboard.chooseCardPayment();
        cardPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getEngTextInMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        cardPayment.enterDataAndContinue(card, month, year, cardholder, code);
        cardPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Отправка формы со специальными символами в поле “Месяц”")
    void declinePurchaseWithDiacriticsInMonth() {
        cardPayment = dashboard.chooseCardPayment();
        cardPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getDiacriticsInField();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        cardPayment.enterDataAndContinue(card, month, year, cardholder, code);
        cardPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Отправка формы с предыдущим месяцем и текущим годом")
    void declinePurchaseWithPreviousMonthAndPresentYear() {
        cardPayment = dashboard.chooseCardPayment();
        cardPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getPreviousMonth();
        var year = DataHelper.getPresentYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        cardPayment.enterDataAndContinue(card, month, year, cardholder, code);
        cardPayment.getWrongYearError();
    }

    @Test
    @DisplayName("Отправка формы с предыдущим годом")
    void declinePurchaseWithPreviousYear() {
        cardPayment = dashboard.chooseCardPayment();
        cardPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getPreviousYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        cardPayment.enterDataAndContinue(card, month, year, cardholder, code);
        cardPayment.getWrongYearError();
    }

    @Test
    @DisplayName("Отправка формы с одним символом в поле “Год”")
    void declinePurchaseWithOneSymbolYear() {
        cardPayment = dashboard.chooseCardPayment();
        cardPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getOneSymbolInYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        cardPayment.enterDataAndContinue(card, month, year, cardholder, code);
        cardPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Отправка формы с неправдоподобно большим сроком карты(на месяц больше 10-ти лет)")
    void declinePurchaseWithMoreThen10YearsTerm() {
        cardPayment = dashboard.chooseCardPayment();
        cardPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getNextMonth();
        var year = DataHelper.getMaximumTermInYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        cardPayment.enterDataAndContinue(card, month, year, cardholder, code);
        cardPayment.getWrongMonthError();
    }

    @Test
    @DisplayName("Отправка формы с пустым полем “Год”")
    void declinePurchaseWithEmptyYear() {
        cardPayment = dashboard.chooseCardPayment();
        cardPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getEmptyField();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        cardPayment.enterDataAndContinue(card, month, year, cardholder, code);
        cardPayment.getEmptyFieldError();
    }

    @Test
    @DisplayName("Отправка формы с кириллицей в поле “Год”")
    void declinePurchaseWithCyrillicLettersInYear() {
        cardPayment = dashboard.chooseCardPayment();
        cardPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getRusTextInYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        cardPayment.enterDataAndContinue(card, month, year, cardholder, code);
        cardPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Отправка формы с латиницей в поле “Год”")
    void declinePurchaseWithEnglishLettersInYear() {
        cardPayment = dashboard.chooseCardPayment();
        cardPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getEngTextInYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        cardPayment.enterDataAndContinue(card, month, year, cardholder, code);
        cardPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Отправка формы со спец. символами в поле “Год”")
    void declinePurchaseWithDiacriticsInYear() {
        cardPayment = dashboard.chooseCardPayment();
        cardPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getDiacriticsInField();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getValidCode();
        cardPayment.enterDataAndContinue(card, month, year, cardholder, code);
        cardPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Ввести данные с использованием кириллицы в поле “Владелец”")
    void declinePurchaseWithCyrillicLettersInOwner() {
        cardPayment = dashboard.chooseCardPayment();
        cardPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getRussianKeyboardNameInOwner();
        var code = DataHelper.getValidCode();
        cardPayment.enterDataAndContinue(card, month, year, cardholder, code);
        cardPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Ввести данные с использованием чисел в поле “Владелец”")
    void declinePurchaseWithNumbersInOwner() {
        cardPayment = dashboard.chooseCardPayment();
        cardPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getNumbersInOwner();
        var code = DataHelper.getValidCode();
        cardPayment.enterDataAndContinue(card, month, year, String.valueOf(cardholder), code);
        cardPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Ввести данные с использованием специальных символов в поле “Владелец”")
    void declinePurchaseWithDiacriticsInOwner() {
        cardPayment = dashboard.chooseCardPayment();
        cardPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getDiacriticsInField();
        var code = DataHelper.getValidCode();
        cardPayment.enterDataAndContinue(card, month, year, cardholder, code);
        cardPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Ввести несколько пробелов в поле “Владелец”")
    void declinePurchaseWithSpacesInOwner() {
        cardPayment = dashboard.chooseCardPayment();
        cardPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getSpacesInField();
        var code = DataHelper.getValidCode();
        cardPayment.enterDataAndContinue(card, month, year, cardholder, code);
        cardPayment.getEmptyFieldError();
    }

    @Test
    @DisplayName("Ввести два символа в поле “CVC/CVV”")
    void declinePurchaseWithTwoSymbolsInCode() {
        cardPayment = dashboard.chooseCardPayment();
        cardPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getCodeWithTwoNumbers();
        cardPayment.enterDataAndContinue(card, month, year, cardholder, code);
        cardPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Отправка формы с пустым полем “CVC/CVV”")
    void declinePurchaseWithEmptyCode() {
        cardPayment = dashboard.chooseCardPayment();
        cardPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getEmptyField();
        cardPayment.enterDataAndContinue(card, month, year, cardholder, code);
        cardPayment.getEmptyFieldError();
    }

    @Test
    @DisplayName("Ввести кириллицу в поле “CVC/CVV”")
    void declinePurchaseWithCyrillicLettersInCode() {
        cardPayment = dashboard.chooseCardPayment();
        cardPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getRusTextInCode();
        cardPayment.enterDataAndContinue(card, month, year, cardholder, code);
        cardPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Ввести латиницу в поле “CVC/CVV”")
    void declinePurchaseWithEnglishLettersInCode() {
        cardPayment = dashboard.chooseCardPayment();
        cardPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getEngTextInCode();
        cardPayment.enterDataAndContinue(card, month, year, cardholder, code);
        cardPayment.getWrongFormatError();
    }

    @Test
    @DisplayName("Ввести спец. символы в поле “CVC/CVV”")
    void declinePurchaseWithDiacriticsInCode() {
        cardPayment = dashboard.chooseCardPayment();
        cardPayment.clearForm();
        var card = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var cardholder = DataHelper.getValidOwner();
        var code = DataHelper.getDiacriticsInField();
        cardPayment.enterDataAndContinue(card, month, year, cardholder, code);
        cardPayment.getWrongFormatError();
    }
}






// БАГИ:
 // 1. неверное название страницы (заявка на карту)
 // 2. если неправильно заполнить поля, нажать продолжить, потом заполнить верно, сообщения под полями об ошибках все равно останутся, пока заявка не будет отправлена на проверку
 // 3. не пропускает карту с указанием максимального года
 // 4. принимает карту в статусе DECLINED - должен отклонять
 // 5. пропускает с нулевым месяцем
 // 6. при предыдущем месяце и текущем годе выдает ошибку Неверно указан срок действия карты вместо ошибки Истёк срок действия карты
 // 7. принимает спец символы в поле владелец
 // 8. принимает числа в поле владелец
// 9. принимает кириллицу в поле владелец
// 10. при пустом поле Год ошибка Неверный формат вместо ошибки Поле должно быть заполнено
// 11. при пустом поле Месяц ошибка Неверный формат вместо ошибки Поле должно быть заполнено
// 12. при пустом поле Номер Карты ошибка Неверный формат вместо ошибки Поле должно быть заполнено