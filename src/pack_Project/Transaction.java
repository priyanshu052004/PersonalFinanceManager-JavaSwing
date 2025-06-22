// File: Transaction.java
package pack_Project;

import java.util.Date;


public class Transaction {
    private int transactionId;
    private int userId;
    private Date date;
    private String type; // "income" or "expense"
    private String category;
    private double amount;
    private String note;

    public Transaction(int transactionId, int userId, Date date, String type, String category, double amount, String note) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.date = date;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.note = note;
    }

    // --- Getters ---
    public int getTransactionId() {
        return transactionId;
    }

    public int getUserId() {
        return userId;
    }

    public Date getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public String getNote() {
        return note;
    }

    // --- Setters ---
    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
