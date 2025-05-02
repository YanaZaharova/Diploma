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
    @DisplayName("Отправка формы с текущим годом в поле 'Год'")
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
    @DisplayName("Отправка формы с максимально далеким годом в поле 'Год'")
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
}





// ВКЛЮЧИТЬ ПРОВЕРКУ ПОД КАКИМ ПОЛЕМ ПОЯВЛЯЕТСЯ СООБЩЕНИЕ:
// попробовать конструкцию по типу monthField.$(".input__sub").ссылка на метод

// БАГИ:
 // 1. неверное название страницы (заявка на карту)
 // 2. если неправильно заполнить поля, нажать продолжить, потом заполнить верно и снова нажать продолжить, сообщения под полями об ошибках все равно останутся
 // 3. не пропускает карту с указанием максимального года
 // 4. принимает карту в статусе DECLINED - должен отклонять
 //
 //
 //
 //


// $(byText("Купить")).click(); - выбрать вариант купить по карте
// $(byText("Купить в кредит")).click(); - выбрать вариант взять в кредит
// $("[class='heading heading_size_m heading_theme_alfa-on-white']")
//                   + shouldHave(exactText("Оплата по карте")) - поиск заголовка раздела оплаты
//                   + shouldHave(exactText("Кредит по данным карты")) - поиск заголовка раздела кредита
// $('[placeholder="0000 0000 0000 0000"]') - поле номер карты
// $('[placeholder="08"]') - поле месяц
// $('[placeholder="22"]') - поле год
// $$('[class="input input_type_text input_view_default input_size_m input_width_available input_has-label input_theme_alfa-on-white"]').shouldHave(exactText("Вледелец"))
// ИЛИ
// $$('[class="input input_type_text input_view_default input_size_m input_width_available input_has-label input_theme_alfa-on-white"]').get(3)
// ИЛИ
// $$("[class='input__box']").get(3)
// ИЛИ
// $$("[class='input__control']").get(3) - поле ВЛАДЕЛЕЦ
// $('[placeholder="999"]') - поле CVV код
// $(byText("Продолжить")).click(); - отправить заявку
// $("[class='notification notification_status_ok notification_has-closer notification_stick-to_right notification_theme_alfa-on-white'] .notification__title")
//       + .shouldHave(text("Успешно")  -  успешная покупка (карта/кредит)
// $('[class="notification notification_status_error notification_has-closer notification_stick-to_right notification_theme_alfa-on-white"] .notification__title')
//       + .shouldHave(text("Ошибка") - отказ в проведении операции
// $(byText("Неверный формат"))
// $(byText("Поле обязательно для заполнения"))
// $(byText("Неверно указан срок действия карты")) - ошибка для поля МЕСЯЦ (когда вводишь номер больше 12 или 00)
// $(byText("Истёк срок действия карты")) - ошибка для поля ГОД
// $(byText("Цифры и специальные символы недопустимы")) ошибка для поля ВЛАДЕЛЕЦ
//