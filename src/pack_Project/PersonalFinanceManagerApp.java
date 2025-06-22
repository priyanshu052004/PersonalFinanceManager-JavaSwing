
package pack_Project;

import javax.swing.*;

public class PersonalFinanceManagerApp {

    private UserDAO userDAO;
    private TransactionDAO transactionDAO;
    private BudgetDAO budgetDAO;

    public PersonalFinanceManagerApp() {
        // we create the object hear
        userDAO = new UserDAO();
        transactionDAO = new TransactionDAO();
        budgetDAO = new BudgetDAO();
    }

//    obj return kr rahe hai
    public UserDAO getUserDAO() {
        return userDAO;
    }


    public TransactionDAO getTransactionDAO() {
        return transactionDAO;
    }


    public BudgetDAO getBudgetDAO() {
        return budgetDAO;
    }

    public static void main(String[] args) {
//       we create the obj of personalFinancialApp obj and through to LoginFram() class
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }

        catch (Exception e)
        {
            System.err.println("Could not set system look and feel: " + e.getMessage());
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            PersonalFinanceManagerApp app = new PersonalFinanceManagerApp();
            new LoginFrame(app); // Pass the application instance to LoginFrame
        });
    }
}
