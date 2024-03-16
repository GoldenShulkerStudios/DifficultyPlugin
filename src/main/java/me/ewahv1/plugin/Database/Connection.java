package me.ewahv1.plugin.Database;

import java.sql.DriverManager;

public class Connection {

    private static java.sql.Connection connection;

    public static java.sql.Connection getConnection() {
        try {
            if (connection == null) {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/InsomniaDB", "root", "*");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}
