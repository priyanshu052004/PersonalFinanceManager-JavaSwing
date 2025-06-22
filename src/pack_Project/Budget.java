package pack_Project;

public class Budget {
    private int budgetId;
    private int userId;
    private String month; // Format: 'YYYY-MM'
    private String category;
    private double limitAmount;

    public Budget(int budgetId, int userId, String month, String category, double limitAmount) {
        this.budgetId = budgetId;
        this.userId = userId;
        this.month = month;
        this.category = category;
        this.limitAmount = limitAmount;
    }

    // Getters
    public int getBudgetId() {
        return budgetId;
    }

    public int getUserId() {
        return userId;
    }

    public String getMonth() {
        return month;
    }

    public String getCategory() {
        return category;
    }

    public double getLimitAmount() {
        return limitAmount;
    }

    // Setters
    public void setBudgetId(int budgetId) {
        this.budgetId = budgetId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setLimitAmount(double limitAmount) {
        this.limitAmount = limitAmount;
    }
}
