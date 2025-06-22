// File: DashboardFrame.java
package pack_Project;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Date;
import java.util.Calendar;
import java.util.stream.Collectors;


public class DashboardFrame extends JFrame {
    private PersonalFinanceManagerApp app;
    private User currentUser;

    private JLabel currentBalanceLabel;
    private JLabel totalIncomeLabel;
    private JLabel totalExpensesLabel;
    private BarChartPanel barChartPanel;


    private JButton addTransactionButton;
    private JButton viewReportsButton;
    private JButton budgetPlanningButton;

    // Categories for quick access
    private String[] expenseCategoriesArray = {"Food", "Rent", "Transport", "Utilities", "Entertainment", "Healthcare", "Other"};
    private String[] incomeCategoriesArray = {"Salary", "Investments", "Gift", "Other Income"};


    public DashboardFrame(PersonalFinanceManagerApp app, User currentUser) {
        this.app = app;
        this.currentUser = currentUser;

        setTitle("Personal Finance Manager - Dashboard for " + currentUser.getUsername());
        setSize(850, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        addListeners();
        refreshDashboard();
        setVisible(true);
    }


    private void initComponents() {
        JPanel dashboardPanel = new JPanel();
        dashboardPanel.setLayout(new BorderLayout(15, 15)); // Spacing between components
        dashboardPanel.setBorder(new EmptyBorder(25, 25, 25, 25)); // Padding around the panel
        dashboardPanel.setBackground(new Color(240, 248, 255)); // Light blue background

        // --- Top Panel: Title and User Info ---
        JPanel topSectionPanel = new JPanel(new BorderLayout());
        topSectionPanel.setOpaque(false); // Transparent background

        JLabel titleLabel = new JLabel("Personal Finance Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(new Color(0, 102, 204));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel userInfoLabel = new JLabel("Logged in as: " + currentUser.getUsername() + " (ID: " + currentUser.getUserId() + ")");
        userInfoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        userInfoLabel.setForeground(Color.DARK_GRAY);
        userInfoLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        topSectionPanel.add(titleLabel, BorderLayout.CENTER);
        topSectionPanel.add(userInfoLabel, BorderLayout.EAST);
        dashboardPanel.add(topSectionPanel, BorderLayout.NORTH);

        // --- Main Content Panel (holds summary, chart, and buttons) ---
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Summary Panels (Balance, Income, Expenses) ---
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new GridLayout(1, 3, 20, 0)); // Increased horizontal spacing
        summaryPanel.setBackground(Color.WHITE);

        currentBalanceLabel = new JLabel();
        totalIncomeLabel = new JLabel();
        totalExpensesLabel = new JLabel();

        summaryPanel.add(createInfoPanel("Current Balance", currentBalanceLabel, new Color(46, 204, 113)));
        summaryPanel.add(createInfoPanel("Total Income", totalIncomeLabel, new Color(52, 152, 219)));
        summaryPanel.add(createInfoPanel("Total Expenses", totalExpensesLabel, new Color(231, 76, 60)));

        contentPanel.add(summaryPanel);
        contentPanel.add(Box.createVerticalStrut(30));

        // --- Chart and Buttons Section ---
        JPanel centerSectionPanel = new JPanel();
        centerSectionPanel.setLayout(new BoxLayout(centerSectionPanel, BoxLayout.X_AXIS));
        centerSectionPanel.setBackground(Color.WHITE);
        centerSectionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Chart Container
        JPanel chartContainer = new JPanel(new BorderLayout());
        chartContainer.setBackground(Color.WHITE);
        chartContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel chartTitle = new JLabel("Monthly Expenses by Category");
        chartTitle.setFont(new Font("Arial", Font.BOLD, 16));
        chartTitle.setForeground(Color.DARK_GRAY);
        chartContainer.add(chartTitle, BorderLayout.NORTH);

        barChartPanel = new BarChartPanel(new int[0], expenseCategoriesArray);
        barChartPanel.setPreferredSize(new Dimension(450, 300)); // chart size
        chartContainer.add(barChartPanel, BorderLayout.CENTER);
        centerSectionPanel.add(chartContainer);
        centerSectionPanel.add(Box.createHorizontalStrut(30));

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Color.WHITE);

        //buttons
        addTransactionButton = createActionButton("Add Transaction", new Color(63, 150, 219));
        viewReportsButton = createActionButton("View Reports", new Color(46, 204, 113));
        budgetPlanningButton = createActionButton("Budget Planning", new Color(243, 156, 18));

        buttonPanel.add(addTransactionButton);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(viewReportsButton);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(budgetPlanningButton);

        centerSectionPanel.add(buttonPanel);
        contentPanel.add(centerSectionPanel);

        dashboardPanel.add(contentPanel, BorderLayout.CENTER);
        add(dashboardPanel);
    }


    private JPanel createInfoPanel(String title, JLabel valueLabel, Color bgColor) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1, true),
                new EmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(bgColor);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        valueLabel.setFont(new Font("Arial", Font.BOLD, 22));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(8));
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(valueLabel);
        panel.add(Box.createVerticalStrut(8));

        return panel;
    }


    private JButton createActionButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 50));
        button.setPreferredSize(new Dimension(200, 50));
        button.setBorder(new LineBorder(bgColor.darker(), 2));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }


    private void addListeners() {
        addTransactionButton.addActionListener(e -> {
            AddTransactionDialog addDialog = new AddTransactionDialog(this, currentUser.getUserId(), expenseCategoriesArray, incomeCategoriesArray, app.getTransactionDAO());
            addDialog.setVisible(true); // Show dialog modally
            refreshDashboard(); // Refresh dashboard after dialog closes (transaction might have been added)
        });

        viewReportsButton.addActionListener(e -> {
            // Fetch fresh transactions for the current user from the database
            List<Transaction> userTransactions = app.getTransactionDAO().getTransactionsByUserId(currentUser.getUserId());
            new ViewReportsFrame(this, userTransactions, expenseCategoriesArray);
        });

        budgetPlanningButton.addActionListener(e -> {
//            CustomMessageBox.showInfoMessage(this, "Budget Planning feature coming soon!", "Feature Coming Soon");
        });
    }


    public void refreshDashboard() {
        double currentBalance = 0.00;
        double totalIncome = 0.00;
        double totalExpenses = 0.00;

        List<Transaction> userTransactions = app.getTransactionDAO().getTransactionsByUserId(currentUser.getUserId());

        for (Transaction t : userTransactions) {
            if ("income".equalsIgnoreCase(t.getType())) {
                totalIncome += t.getAmount();
            } else if ("expense".equalsIgnoreCase(t.getType())) {
                totalExpenses += t.getAmount();
            }
        }
        currentBalance = totalIncome - totalExpenses;

        // Update summary labels to display in Rupees
        currentBalanceLabel.setText(String.format("₹ %.2f", currentBalance));
        totalIncomeLabel.setText(String.format("₹ %.2f", totalIncome));
        totalExpensesLabel.setText(String.format("₹ %.2f", totalExpenses));

        // Update the bar chart data and repaint
        barChartPanel.setChartData(getExpenseDataForChart(userTransactions)); // Pass filtered transactions
        barChartPanel.repaint();
    }


    private int[] getExpenseDataForChart(List<Transaction> userTransactions) {

        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH);
        int currentYear = cal.get(Calendar.YEAR);

        List<Transaction> userMonthlyExpenses = userTransactions.stream()
                .filter(t -> "expense".equalsIgnoreCase(t.getType()) &&
                        isSameMonthAndYear(t.getDate(), new Date())) // Compare with current date
                .collect(Collectors.toList());


        double[] expenseValuesDouble = new double[expenseCategoriesArray.length];
        for (Transaction t : userMonthlyExpenses) {
            for (int i = 0; i < expenseCategoriesArray.length; i++) {
                // Case-insensitive comparison for category
                if (expenseCategoriesArray[i].equalsIgnoreCase(t.getCategory())) {
                    expenseValuesDouble[i] += t.getAmount();
                    break;
                }
            }
        }


        int[] expenseValuesInt = new int[expenseCategoriesArray.length];
        double maxVal = 0;
        for (double val : expenseValuesDouble) {
            if (val > maxVal) maxVal = val;
        }

        if (maxVal == 0) {
            return new int[expenseCategoriesArray.length];
        }

        for (int i = 0; i < expenseValuesDouble.length; i++) {

            expenseValuesInt[i] = (int) ((expenseValuesDouble[i] / maxVal) * 100);
            if (expenseValuesInt[i] < 1 && expenseValuesDouble[i] > 0) {
                expenseValuesInt[i] = 1;
            }
        }
        return expenseValuesInt;
    }


    private boolean isSameMonthAndYear(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
    }
}
