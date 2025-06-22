// File: DatabaseConnection.java
package pack_Project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class provides a static method to get a database connection
 */
public class DatabaseConnection {


    private static final String DB_URL = "jdbc:mysql://localhost:3306/finance_manager";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Priyanshu@1234";
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";



//    return the connection object if successful otherwise null return
    public static Connection getConnection() {
//        initially we declear the connection obj conn null
        Connection conn = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException se) {

            System.err.println("SQL Exception during database connection:");
            se.printStackTrace();
            CustomMessageBox.showErrorMessage(null, "Error to connect to database", "Database Connection Error");
        } catch (ClassNotFoundException e) {
            // Handle errors for Class.forName
            System.err.println("JDBC Driver not found");
            e.printStackTrace();
            CustomMessageBox.showErrorMessage(null, "Error to connect to database", "Driver Error");
        }
        return conn;
    }


    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
