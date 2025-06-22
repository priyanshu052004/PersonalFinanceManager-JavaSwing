// File: UserDAO.java
package pack_Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//handel the database opration
public class UserDAO {


    public User authenticateUser(String username, String password) {
        String sql = "SELECT user_id, username, password, email FROM users WHERE username = ? AND password = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) {
                return null;
            }
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email")
                );
            }
        } catch (SQLException se) {
            System.err.println("Error authenticating user: " + se.getMessage());
            se.printStackTrace();
            CustomMessageBox.showErrorMessage(null, "Database error during login", "Login Error");
        }
        finally
        {
            // Close resources in reverse order of creation
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null) pstmt.close();
            }
            catch (SQLException se)
            {
                se.printStackTrace();
            }
            DatabaseConnection.closeConnection(conn); // Close the connection
        }
        return user;
    }
}
