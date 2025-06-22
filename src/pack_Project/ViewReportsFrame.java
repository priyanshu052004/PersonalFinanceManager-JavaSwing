// File: ViewReportsFrame.java
package pack_Project;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*; // Keep java.awt.Font for Swing components
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.stream.Collectors;

// Import OpenPDF classes
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Font;


public class ViewReportsFrame extends JFrame {
    private List<Transaction> transactions;
    private String[] expenseCategories;

    private JTable transactionTable;
    private DefaultTableModel tableModel;
    private PieChartPanel pieChartPanel;
    private JButton exportButton;

    public ViewReportsFrame(JFrame parent, List<Transaction> userTransactions, String[] expenseCategories) {
        super("Personal Finance Manager - Reports");
        this.transactions = userTransactions;
        this.expenseCategories = expenseCategories;

        setSize(900, 700);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);

        initComponents();
        populateTable();
        updatePieChart();
        addListeners();
        setVisible(true);
    }


    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 248, 255));

        JLabel reportTitle = new JLabel("Financial Reports");
        reportTitle.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 28));
        reportTitle.setForeground(new Color(0, 102, 204));
        reportTitle.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(reportTitle, BorderLayout.NORTH);

        // --- Transaction Table ---
        String[] columnNames = {"ID", "Date", "Type", "Category", "Amount", "Note"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        transactionTable = new JTable(tableModel);
        transactionTable.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));
        transactionTable.setRowHeight(25);
        transactionTable.getTableHeader().setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        transactionTable.getTableHeader().setBackground(new Color(220, 230, 240));
        transactionTable.setFillsViewportHeight(true);

        JScrollPane tableScrollPane = new JScrollPane(transactionTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                "All Transactions", 0, 0, new java.awt.Font("Arial", java.awt.Font.BOLD, 14), Color.DARK_GRAY));
        tableScrollPane.setPreferredSize(new Dimension(800, 300));
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);


        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setOpaque(false);

        JPanel chartsPanel = new JPanel(new BorderLayout());
        chartsPanel.setBackground(Color.WHITE);
        chartsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                "Expense Breakdown", 0, 0, new java.awt.Font("Arial", java.awt.Font.BOLD, 14), Color.DARK_GRAY));

        pieChartPanel = new PieChartPanel();
        pieChartPanel.setPreferredSize(new Dimension(400, 300));
        chartsPanel.add(pieChartPanel, BorderLayout.CENTER);

        bottomPanel.add(chartsPanel, BorderLayout.CENTER);

        // Export button panel
        JPanel exportButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        exportButtonPanel.setOpaque(false);
        exportButton = createActionButton("Export to PDF", new Color(0, 150, 136));
        exportButtonPanel.add(exportButton);

        bottomPanel.add(exportButtonPanel, BorderLayout.SOUTH);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }


    private JButton createActionButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(150, 40));
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
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportReportToPdf();
            }
        });
    }


    private void populateTable() {
        tableModel.setRowCount(0);

        transactions.sort(Comparator.comparing(Transaction::getDate).reversed());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Transaction t : transactions) {
            Object[] rowData = {
                    t.getTransactionId(),
                    sdf.format(t.getDate()),
                    t.getType(),
                    t.getCategory(),
                    String.format("₹ %.2f", t.getAmount()),
                    t.getNote() != null ? t.getNote() : "" // Ensure note is not null
            };
            tableModel.addRow(rowData);
        }
    }


    private void updatePieChart() {
        List<Transaction> expenses = transactions.stream()
                .filter(t -> "expense".equalsIgnoreCase(t.getType()))
                .collect(Collectors.toList());

        Map<String, Double> categoryExpenses = expenses.stream()
                .collect(Collectors.groupingBy(Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)));

        String[] chartNames = new String[categoryExpenses.size()];
        double[] chartValues = new double[categoryExpenses.size()];
        int i = 0;
        for (Map.Entry<String, Double> entry : categoryExpenses.entrySet()) {
            chartNames[i] = entry.getKey();
            chartValues[i] = entry.getValue();
            i++;
        }

        pieChartPanel.setChartData(chartNames, chartValues);
        pieChartPanel.repaint();
    }

    /**
     * Exports the transaction data to a PDF file.
     * Uses JFileChooser to allow the user to select the save location.
     */
    private void exportReportToPdf() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Report to PDF");
        fileChooser.setSelectedFile(new File("PersonalFinanceReport_" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".pdf"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().toLowerCase().endsWith(".pdf")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".pdf");
            }

            Document document = new Document(PageSize.A4);
            try {
                PdfWriter.getInstance(document, new FileOutputStream(fileToSave));
                document.open();

                Font titleFont = new Font(Font.HELVETICA, 24, Font.BOLD, new Color(0, 102, 204));
                Paragraph title = new Paragraph("Personal Finance Report", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                title.setSpacingAfter(20);
                document.add(title);

                Font dateFont = new Font(Font.HELVETICA, 12, Font.ITALIC, Color.GRAY);
                Paragraph reportDate = new Paragraph("Report Date: " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()), dateFont);
                reportDate.setAlignment(Element.ALIGN_RIGHT);
                reportDate.setSpacingAfter(10);
                document.add(reportDate);


                document.add(new Paragraph("All Transactions:", new Font(Font.HELVETICA, 16, Font.BOLD)));
                document.add(new Paragraph(" "));

                PdfPTable pdfTable = new PdfPTable(tableModel.getColumnCount());
                pdfTable.setWidthPercentage(100);
                pdfTable.setSpacingBefore(10f);
                pdfTable.setSpacingAfter(10f);

                float[] columnWidths = {0.8f, 1.2f, 0.8f, 1.2f, 1.0f, 2.0f};
                pdfTable.setWidths(columnWidths);

                Font headerFont = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);
                Color headerBgColor = new Color(63, 150, 219);
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    PdfPCell headerCell = new PdfPCell(new Phrase(tableModel.getColumnName(i), headerFont));
                    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    headerCell.setBackgroundColor(headerBgColor);
                    headerCell.setPadding(5);
                    pdfTable.addCell(headerCell);
                }

                Font dataFont = new Font(Font.HELVETICA, 9);
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        // Safely get the value from the table model
                        Object cellValue = tableModel.getValueAt(i, j);
                        String cellText = (cellValue != null) ? cellValue.toString() : ""; // Convert null to empty string

                        PdfPCell dataCell = new PdfPCell(new Phrase(cellText, dataFont));
                        dataCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        dataCell.setPadding(4);
                        if (i % 2 == 0) {
                            dataCell.setBackgroundColor(new Color(245, 245, 245));
                        }
                        pdfTable.addCell(dataCell);
                    }
                }
                document.add(pdfTable);

                CustomMessageBox.showInfoMessage(this, "Report saved successfully to: \n" + fileToSave.getAbsolutePath(), "Export Successful");

            } catch (Exception ex) {
                System.err.println("Error exporting report to PDF: " + ex.getMessage());
                ex.printStackTrace();
                CustomMessageBox.showErrorMessage(this, "Failed to export report to PDF: " + ex.getMessage(), "Export Error");
            } finally {
                if (document.isOpen()) {
                    document.close();
                }
            }
        }
    }
}
