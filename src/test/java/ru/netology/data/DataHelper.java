package ru.netology.data;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {
    private static Faker engFaker = new Faker(new Locale("en"));
    private static Faker rusFaker = new Faker(new Locale("ru"));

    private DataHelper() {
    }

    public static String getFirstCardNumber() {
        return "4444 4444 4444 4441";
    }

    public static String getSecondCardNumber() {
        return "4444 4444 4444 4442";
    }

    public static String getFirstCardStatus() {
        return "APPROVED";
    }

    public static String getDeclinedCardStatus() {
        return "DECLINED";
    }

    public static String getValidMonth() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getValidOwner() {
        return engFaker.name().firstName() + " " + engFaker.name().lastName();
    }

    public static String getValidYear() {
        return LocalDate.now().plusYears(3).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getValidCode() {
        return rusFaker.number().digits(3);
    }

    public static String getOneSymbolCard() {
        return rusFaker.number().digits(1);
    }

    public static String get15SymbolCard() {
        return rusFaker.number().digits(15);
    }

    public static String getRandomCardNumber() {
        return rusFaker.business().creditCardNumber();
    }

    public static String getOneSymbolInMonth() {
        return rusFaker.number().digits(1);
    }

    public static String get13InMonth() {
        return "13";
    }

    public static String getZerosInMonth() {
        return "00";
    }

    public static String getOneSymbolInYear() {
        return rusFaker.number().digits(1);
    }

    public static String get11YearsMoreThanCurrentYear() {
        return LocalDate.now().plusYears(11).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getMaximumTermInYear() {
        return LocalDate.now().plusYears(10).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getPresentYear() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getPreviousYear() {
        return LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getCyrillicSymbolsInOwner() {
        return rusFaker.name().firstName() + " " + rusFaker.name().lastName();
    }

    public static int getNumbersInOwner() {
        return rusFaker.number().randomDigit();
    }

    public static String getDiacriticsInField() {
        return "$*@#&^";
    }

    public static String getSpacesInField() {
        return "         ";
    }

    public static String getCodeWithTwoNumbers() {
        return rusFaker.number().digits(2);
    }

    public static String getEmptyField() {
        return "";
    }
}
