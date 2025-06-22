// File: BarChartPanel.java
package pack_Project;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BarChartPanel extends JPanel {
    private int[] expenseValues;
    private String[] expenseCategories;

    public BarChartPanel(int[] values, String[] categories) {
        this.expenseValues = values;
        this.expenseCategories = categories;
        setBackground(Color.WHITE);
    }


    public void setChartData(int[] values) {
        this.expenseValues = values;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int padding = 40;
        int labelPadding = 25;

        int chartWidth = width - 2 * padding;
        int chartHeight = height - 2 * padding - labelPadding;


        if (expenseCategories == null || expenseCategories.length == 0 || expenseValues == null || expenseValues.length == 0) {
            g2d.setColor(Color.DARK_GRAY);
            String noDataMsg = "No Expense Data Available";
            FontMetrics fm = g2d.getFontMetrics();
            int stringWidth = fm.stringWidth(noDataMsg);
            g2d.drawString(noDataMsg, (width - stringWidth) / 2, height / 2);
            return;
        }


        int maxValue = Arrays.stream(expenseValues).max().orElse(0);
        if (maxValue == 0) maxValue = 1;

        // Draw X and Y axes
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawLine(padding, padding + chartHeight, padding, padding);
        g2d.drawLine(padding, padding + chartHeight, padding + chartWidth, padding + chartHeight);
        g2d.setStroke(new BasicStroke(1f));

        g2d.setColor(new Color(230, 230, 230));
        int numYLabels = 5;
        for (int i = 0; i <= numYLabels; i++) {
            int y = padding + chartHeight - (i * chartHeight / numYLabels);
            g2d.drawLine(padding, y, padding + chartWidth, y);


            double actualValue = (double)maxValue / numYLabels * i;
            String yLabel = String.format("%.0f", actualValue);
            g2d.setColor(Color.GRAY);
            g2d.drawString(yLabel, padding - g2d.getFontMetrics().stringWidth(yLabel) - 5, y + 5);
        }

        int barSpacing = 15;
        int totalBarsWidth = chartWidth - (expenseCategories.length - 1) * barSpacing;
        if (expenseCategories.length == 1) {
            totalBarsWidth = chartWidth / 2;
        }
        int barWidth = totalBarsWidth / expenseCategories.length;
        barWidth = Math.max(10, barWidth);


        for (int i = 0; i < expenseCategories.length; i++) {
            int value = expenseValues[i];
            int barHeight = (int) ((double) value / maxValue * chartHeight);
            if (value > 0 && barHeight == 0) barHeight = 1;

            int x = padding + i * (barWidth + barSpacing);
            if (expenseCategories.length == 1) {
                x = padding + (chartWidth / 2) - (barWidth / 2); // Center single bar
            }

            int y = padding + chartHeight - barHeight;

            // Bar color
            g2d.setColor(new Color(63, 150, 219)); // Blue color
            g2d.fill(new Rectangle2D.Double(x, y, barWidth, barHeight));
            g2d.setColor(new Color(40, 100, 150)); // Darker blue border
            g2d.draw(new Rectangle2D.Double(x, y, barWidth, barHeight));

            // Category label
            g2d.setColor(Color.DARK_GRAY);
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(expenseCategories[i]);

            if (textWidth > barWidth || expenseCategories.length > 5) {
                Graphics2D g2dTemp = (Graphics2D) g2d.create();
                g2dTemp.translate(x + barWidth / 2, height - padding + fm.getHeight() / 2);
                g2dTemp.rotate(Math.toRadians(-45)); // Rotate by -45 degrees
                g2dTemp.drawString(expenseCategories[i], -textWidth / 2, 0);
                g2dTemp.dispose();
            } else {
                g2d.drawString(expenseCategories[i], x + (barWidth - textWidth) / 2, height - padding);
            }
        }
    }
}
