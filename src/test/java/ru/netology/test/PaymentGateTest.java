package ru.netology.test;

public class PaymentGateTest {
}





// ВКЛЮЧИТЬ ПРОВЕРКУ ПОД КАКИМ ПОЛЕМ ПОЯВЛЯЕТСЯ СООБЩЕНИЕ:
// попробовать конструкцию по типу monthField.$(".input__sub").ссылка на метод

// БАГИ:
 // 1. неверное название страницы (заявка на карту)
 // 2. если неправильно заполнить поля, нажать продолжить, потом заполнить верно и снова нажать продолжить, сообщения под полями об ошибках все равно останутся
 //
 //
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