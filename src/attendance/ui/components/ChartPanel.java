package attendance.ui.components;

import attendance.ui.theme.ThemeColors;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Theme-aware chart panel for bar and line charts.
 */
public class ChartPanel extends JPanel {
    private String[] labels;
    private double[] values;
    private ChartType type;
    private Color chartColor;

    public enum ChartType {
        BAR, LINE
    }

    public ChartPanel(String[] labels, double[] values, ChartType type, Color color) {
        this.labels = labels;
        this.values = values;
        this.type = type;
        this.chartColor = color;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int padding = ThemeColors.PADDING_CARD;
        int chartWidth = width - padding * 2;
        int chartHeight = height - padding * 2 - 30; // Leave room for labels

        // Find max value
        double maxValue = 0;
        for (double v : values) {
            maxValue = Math.max(maxValue, v);
        }
        if (maxValue == 0)
            maxValue = 100;

        if (type == ChartType.BAR) {
            drawBarChart(g2, padding, padding, chartWidth, chartHeight, maxValue);
        } else {
            drawLineChart(g2, padding, padding, chartWidth, chartHeight, maxValue);
        }

        // Draw labels
        g2.setFont(ThemeColors.FONT_SMALL);
        g2.setColor(ThemeColors.TEXT_MUTED);
        int labelY = height - padding / 2;
        int barWidth = chartWidth / values.length;
        for (int i = 0; i < labels.length; i++) {
            FontMetrics fm = g2.getFontMetrics();
            int labelX = padding + i * barWidth + (barWidth - fm.stringWidth(labels[i])) / 2;
            g2.drawString(labels[i], labelX, labelY);
        }

        g2.dispose();
    }

    private void drawBarChart(Graphics2D g2, int x, int y, int width, int height, double maxValue) {
        int barCount = values.length;
        int gap = ThemeColors.GAP_SMALL;
        int barWidth = (width - gap * (barCount + 1)) / barCount;
        int radius = ThemeColors.BORDER_RADIUS_SMALL;

        for (int i = 0; i < barCount; i++) {
            int barHeight = (int) ((values[i] / maxValue) * height);
            int barX = x + gap + i * (barWidth + gap);
            int barY = y + height - barHeight;

            // Glow effect (if enabled)
            if (ThemeColors.HAS_GLOW && barHeight > 0) {
                for (int j = 8; j > 0; j -= 2) {
                    float alpha = 0.1f * (1f - (float) j / 8);
                    g2.setColor(ThemeColors.withAlpha(chartColor, (int) (alpha * 100)));
                    g2.fillRoundRect(barX - j / 2, barY - j / 2, barWidth + j, barHeight + j, radius, radius);
                }
            }

            // Bar gradient/solid
            if (ThemeColors.HAS_GRADIENTS) {
                g2.setPaint(new GradientPaint(
                        barX, barY, chartColor,
                        barX, barY + barHeight, chartColor.darker()));
            } else {
                g2.setColor(chartColor);
            }
            g2.fillRoundRect(barX, barY, barWidth, barHeight, radius, radius);

            // Value label
            g2.setFont(ThemeColors.FONT_SMALL);
            g2.setColor(ThemeColors.TEXT_PRIMARY);
            String valueStr = String.format("%.0f", values[i]);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(valueStr, barX + (barWidth - fm.stringWidth(valueStr)) / 2, barY - 5);
        }
    }

    private void drawLineChart(Graphics2D g2, int x, int y, int width, int height, double maxValue) {
        int pointCount = values.length;
        int stepX = width / (pointCount - 1);

        int[] xPoints = new int[pointCount];
        int[] yPoints = new int[pointCount];

        for (int i = 0; i < pointCount; i++) {
            xPoints[i] = x + i * stepX;
            yPoints[i] = y + height - (int) ((values[i] / maxValue) * height);
        }

        // Fill area under line (if gradients enabled)
        if (ThemeColors.HAS_GRADIENTS) {
            int[] fillX = new int[pointCount + 2];
            int[] fillY = new int[pointCount + 2];
            System.arraycopy(xPoints, 0, fillX, 0, pointCount);
            System.arraycopy(yPoints, 0, fillY, 0, pointCount);
            fillX[pointCount] = x + width;
            fillY[pointCount] = y + height;
            fillX[pointCount + 1] = x;
            fillY[pointCount + 1] = y + height;

            g2.setPaint(new GradientPaint(
                    x, y, ThemeColors.withAlpha(chartColor, 80),
                    x, y + height, ThemeColors.withAlpha(chartColor, 10)));
            g2.fillPolygon(fillX, fillY, pointCount + 2);
        }

        // Glow on line (if enabled)
        if (ThemeColors.HAS_GLOW) {
            g2.setColor(ThemeColors.withAlpha(chartColor, 40));
            g2.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawPolyline(xPoints, yPoints, pointCount);
        }

        // Line
        g2.setColor(chartColor);
        g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawPolyline(xPoints, yPoints, pointCount);

        // Points
        int dotSize = 8;
        for (int i = 0; i < pointCount; i++) {
            if (ThemeColors.HAS_GLOW) {
                g2.setColor(ThemeColors.withAlpha(chartColor, 60));
                g2.fillOval(xPoints[i] - dotSize, yPoints[i] - dotSize, dotSize * 2, dotSize * 2);
            }
            g2.setColor(chartColor);
            g2.fillOval(xPoints[i] - dotSize / 2, yPoints[i] - dotSize / 2, dotSize, dotSize);
            g2.setColor(ThemeColors.BG_DARK);
            g2.fillOval(xPoints[i] - 2, yPoints[i] - 2, 4, 4);
        }
    }
}
