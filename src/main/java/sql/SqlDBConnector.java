package sql;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

public class SqlDBConnector {

    private static DataSource datasource;
    private static Connection connection = null;

    private static String dbName = "administrator";
    private static String username = "administrator";
    private static String password = "firebug";
    private static String host = "188.226.136.226";
    private static int port = 5432;
    //localhost
//    private static String dbName = "postgres";
//    private static String username = "postgres";
//    private static String password = "postgres";
//    private static String host = "127.0.0.1";
//    private static int port = 5432;

    private SqlDBConnector() {
        connection = initializeClient();
    }
    public static synchronized Connection setSource(DataSource ds)
    {
        datasource = ds;
           try {        
                connection = datasource.getConnection();
            } catch (SQLException ex) {
                Logger.getLogger(SqlDBConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
           return connection;
    }

    public static synchronized Connection getDBConnection() {
        if (connection == null) {

            if(datasource == null){  connection = initializeClient(); return connection;}
              try {       
                  
                connection = datasource.getConnection();
                
                  return connection;
            } catch (SQLException ex) {
                System.out.println("Error in connection :"+ex);
                Logger.getLogger(SqlDBConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return connection;
    }

    private static Connection initializeClient() {
        try {

            connection = DriverManager.getConnection(
                    "jdbc:postgresql://" + host + ":" + port + "/" + dbName, username,
                    password);

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        
        return connection;
    }
    
    

}
