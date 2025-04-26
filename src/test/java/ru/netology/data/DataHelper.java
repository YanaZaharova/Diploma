package ru.netology.data;

import com.github.javafaker.Faker;

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

    public static String getSecondCardStatus() {
        return "DECLINED";
    }

    public static String getValidMonth() {}

    public static String getValidYear() {}

    public static String getValidOwner() {}

    public static String getValidCode() {}

    public static String getEmptyCardNumber() {
        return "";
    }

    public static String getOneSymbolCard() {

    }

    public static String get15SymbolCard() {

    }

    public static String getRandomCardNumber() {
        return rusFaker.business().creditCardNumber();
    }

    public static String getOneSymbolInMonth() {}

    public static String get13InMonth() {}

    public static String getZerosInMonth() {}

    public static String getEmptyMonth() {}

    public static String getOneSymbolInYear() {}

    public static String get11YearsMoreThanCurrentYear() {}

    public static String getMaximumTermInYear() {}

    public static String getPresentYear() {}

    public static String getPreviousYear() {}

    public static String getEmptyYear() {}

    public static String getCyrillicSymbolsInOwner() {}

    public static String getNumbersInOwner() {}

    public static String getDiacriticsInOwner() {}

    public static String getSpacesInOwner() {}

    public static String getCodeWithTwoNumbers() {}

    public static String getEmptyCode() {}
}
