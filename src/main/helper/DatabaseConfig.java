package main.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {

    //jdbc:postgresql://localhost:5432/tourismagency
    public static final String DB_URL = "jdbc:postgresql://localhost:5432/tourismagency?TimeZone=Europe/Istanbul";
    public static final String DB_USER = "postgres";
    public static final String DB_PASSWORD = "1";

    private static Connection connection;


    public static Connection connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        }
        return connection;
    }



}
