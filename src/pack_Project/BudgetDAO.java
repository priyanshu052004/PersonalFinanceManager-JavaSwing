// File: BudgetDAO.java
package pack_Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class BudgetDAO {


    public List<Budget> getBudgetsByUserId(int userId) {
        List<Budget> budgets = new ArrayList<>();
        String sql = "SELECT budget_id, user_id, month, category, limit_amount FROM budgets WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return budgets;

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                budgets.add(new Budget(
                        rs.getInt("budget_id"),
                        rs.getInt("user_id"),
                        rs.getString("month"),
                        rs.getString("category"),
                        rs.getDouble("limit_amount")
                ));
            }
        } catch (SQLException se) {
            System.err.println("Error retrieving budgets: " + se.getMessage());
            se.printStackTrace();
            CustomMessageBox.showErrorMessage(null, "Database error retrieving budgets. Check console for details.", "Budget Data Error");
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
            DatabaseConnection.closeConnection(conn);
        }
        return budgets;
    }

}
