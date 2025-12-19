package attendance.ui.components;

import attendance.ui.theme.ThemeColors;

import javax.swing.*;
import java.awt.*;

/**
 * Theme-aware animated circular progress bar for attendance percentage.
 */
public class CircularProgressBar extends JPanel {
    private double percentage = 0;
    private double displayPercentage = 0;
    private Color progressColor = ThemeColors.ACCENT_CYAN;
    private Timer animationTimer;

    public CircularProgressBar() {
        setOpaque(false);
        setPreferredSize(new Dimension(160, 160));
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;

        if (ThemeColors.HAS_ANIMATIONS) {
            // Animate to new percentage
            if (animationTimer != null && animationTimer.isRunning()) {
                animationTimer.stop();
            }

            animationTimer = new Timer(16, e -> {
                if (displayPercentage < percentage) {
                    displayPercentage = Math.min(percentage, displayPercentage + 1);
                    repaint();
                } else if (displayPercentage > percentage) {
                    displayPercentage = Math.max(percentage, displayPercentage - 1);
                    repaint();
                } else {
                    ((Timer) e.getSource()).stop();
                }
            });
            animationTimer.start();
        } else {
            displayPercentage = percentage;
            repaint();
        }
    }

    public void setProgressColor(Color color) {
        this.progressColor = color;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int size = Math.min(getWidth(), getHeight());
        int x = (getWidth() - size) / 2;
        int y = (getHeight() - size) / 2;
        int strokeWidth = 12;
        int arcSize = size - strokeWidth;

        // Background arc
        g2.setColor(ThemeColors.BG_LIGHT);
        g2.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawArc(x + strokeWidth / 2, y + strokeWidth / 2, arcSize, arcSize, 90, -360);

        // Progress arc
        int arcAngle = (int) (displayPercentage * 3.6);

        if (ThemeColors.HAS_GLOW) {
            // Glow effect behind progress
            for (int i = ThemeColors.GLOW_RADIUS; i > 0; i -= 4) {
                float alpha = ThemeColors.GLOW_INTENSITY * (1f - (float) i / ThemeColors.GLOW_RADIUS);
                g2.setColor(ThemeColors.withAlpha(progressColor, (int) (alpha * 60)));
                g2.setStroke(new BasicStroke(strokeWidth + i, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawArc(x + strokeWidth / 2, y + strokeWidth / 2, arcSize, arcSize, 90, -arcAngle);
            }
        }

        // Gradient progress (if enabled)
        if (ThemeColors.HAS_GRADIENTS) {
            g2.setPaint(new GradientPaint(
                    x, y, progressColor,
                    x + arcSize, y + arcSize, ThemeColors.GRADIENT_END));
        } else {
            g2.setColor(progressColor);
        }
        g2.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawArc(x + strokeWidth / 2, y + strokeWidth / 2, arcSize, arcSize, 90, -arcAngle);

        // Percentage text
        String percentText = String.format("%.1f%%", displayPercentage);
        g2.setFont(ThemeColors.FONT_SUBTITLE);
        g2.setColor(ThemeColors.TEXT_PRIMARY);
        FontMetrics fm = g2.getFontMetrics();
        int textX = (getWidth() - fm.stringWidth(percentText)) / 2;
        int textY = (getHeight() + fm.getAscent()) / 2 - 4;
        g2.drawString(percentText, textX, textY);

        g2.dispose();
    }

    /**
     * Get status color based on attendance thresholds
     */
    public static Color getStatusColor(double percentage, double minThreshold, double dangerThreshold) {
        if (percentage >= minThreshold) {
            return ThemeColors.STATUS_SAFE;
        } else if (percentage >= dangerThreshold) {
            return ThemeColors.STATUS_WARNING;
        } else {
            return ThemeColors.STATUS_DANGER;
        }
    }
}
