package sql;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class SqlDBConnector {

    private static Connection connection = null;

    private static String dbName = "administrator";
    private static String username = "administrator";
    private static String password = "firebug";

    private static String host = "188.226.136.226";
    private static int port = 5432;

    private SqlDBConnector() {
        connection = initializeClient();
    }

    public static synchronized Connection getDBConnection() {
        if (connection == null) {
            connection = initializeClient();
        }
        return connection;
    }

    private static Connection initializeClient() {
        try {

            connection = DriverManager.getConnection(
                    "jdbc:postgresql://188.226.136.226:5432/" + dbName, username,
                    password);

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        
        return connection;
    }

}
