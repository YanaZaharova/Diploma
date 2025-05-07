package ru.netology.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLHelper {

    private static final QueryRunner runner = new QueryRunner();
    private static final String url = System.getProperty("db.url");

    private SQLHelper() {
    }

    @SneakyThrows
    private static Connection getConnection() {
        return DriverManager.getConnection(url, "app", "pass");
    }

    @SneakyThrows
    public static void cleanAllData() {
        var request = "DELETE FROM credit_request_entity;";
        var order = "DELETE FROM order_entity;";
        var payment = "DELETE FROM payment_entity;";
        try (var conn = getConnection()) {
            runner.update(conn, request);
            runner.update(conn, order);
            runner.update(conn, payment);
        }
    }

    @SneakyThrows
    public static String getRequestStatusCardPayment() {
        QueryRunner runner = new QueryRunner();
        String SQLRequest = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1";
        try (var conn = getConnection()) {
            return runner.query(conn, SQLRequest, new ScalarHandler<>());
        }
    }

    @SneakyThrows
    public static String getRequestStatusCreditPayment() {
        QueryRunner runner = new QueryRunner();
        String SQLRequest = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1";
        try (var conn = getConnection()) {
            return runner.query(conn, SQLRequest, new ScalarHandler<>());
        }
    }
}
