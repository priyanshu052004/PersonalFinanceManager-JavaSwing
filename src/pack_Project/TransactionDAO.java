// File: TransactionDAO.java
package pack_Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

public class TransactionDAO {


    public boolean addTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (user_id, date, type, category, amount, note) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return false;

            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, transaction.getUserId());
            pstmt.setDate(2, new Date(transaction.getDate().getTime()));
            pstmt.setString(3, transaction.getType());
            pstmt.setString(4, transaction.getCategory());
            pstmt.setDouble(5, transaction.getAmount());
            pstmt.setString(6, transaction.getNote());

            int rowsAffected = pstmt.executeUpdate();
//auto genrated transaction id
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        transaction.setTransactionId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException se) {
            System.err.println("Error adding transaction: " + se.getMessage());
            se.printStackTrace();
            CustomMessageBox.showErrorMessage(null, "Database error adding transaction.", "Transaction Error");
        }
        finally
        {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
            DatabaseConnection.closeConnection(conn);
        }
        return false;
    }
//retrive the transaction uisng user_id

    public List<Transaction> getTransactionsByUserId(int userId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT transaction_id, user_id, date, type, category, amount, note FROM transactions WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return transactions;

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                transactions.add(new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getInt("user_id"),
                        rs.getDate("date"),
                        rs.getString("type"),
                        rs.getString("category"),
                        rs.getDouble("amount"),
                        rs.getString("note")
                ));
            }
        } catch (SQLException se) {
            System.err.println("Error retrieving transactions: " + se.getMessage());
            se.printStackTrace();
            CustomMessageBox.showErrorMessage(null, "Database error retrieving transactions. Check console for details.", "Data Retrieval Error");
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
            DatabaseConnection.closeConnection(conn);
        }
        return transactions;
    }


}
