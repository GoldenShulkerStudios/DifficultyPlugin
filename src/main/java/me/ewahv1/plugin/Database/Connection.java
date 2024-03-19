package me.ewahv1.plugin.Database;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection {

    private static java.sql.Connection connection;

    public static java.sql.Connection getConnection() {
        try {
            if (connection == null) {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/insomniadb", "root", "70284412B*");
                //connection = DriverManager.getConnection("jdbc:mysql://mysql-134847-0.cloudclusters.net:10005/insomniadb", "admin", "Ft9oZVF1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
