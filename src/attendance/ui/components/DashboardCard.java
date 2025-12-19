package attendance.ui.components;

import attendance.ui.theme.ThemeColors;

import javax.swing.*;
import java.awt.*;

/**
 * Theme-aware dashboard card component with icon, value, and label.
 */
public class DashboardCard extends JPanel {
    private String label;
    private String value;
    private String icon;
    private Color accentColor;

    public DashboardCard(String label, String value, String icon, Color accentColor) {
        this.label = label;
        this.value = value;
        this.icon = icon;
        this.accentColor = accentColor;

        setOpaque(false);
        setPreferredSize(new Dimension(ThemeColors.CARD_MIN_WIDTH, ThemeColors.CARD_MIN_HEIGHT));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int radius = ThemeColors.BORDER_RADIUS_MEDIUM;
        int padding = ThemeColors.PADDING_CARD;

        // Shadow (if enabled)
        if (ThemeColors.HAS_SHADOWS) {
            g2.setColor(ThemeColors.SHADOW_COLOR);
            g2.fillRoundRect(
                    ThemeColors.SHADOW_OFFSET_X,
                    ThemeColors.SHADOW_OFFSET_Y + 2,
                    width - 2, height - 2, radius, radius);
        }

        // Card background
        if (ThemeColors.HAS_GRADIENTS) {
            GradientPaint gradient = new GradientPaint(
                    0, 0, ThemeColors.BG_CARD,
                    0, height, ThemeColors.withAlpha(ThemeColors.BG_LIGHT, 180));
            g2.setPaint(gradient);
        } else {
            g2.setColor(ThemeColors.BG_CARD);
        }
        g2.fillRoundRect(0, 0, width, height, radius, radius);

        // Glassmorphism highlight (if enabled)
        if (ThemeColors.HAS_GLASSMORPHISM) {
            g2.setColor(ThemeColors.withAlpha(Color.WHITE, (int) (ThemeColors.GLASS_OPACITY * 255)));
            g2.fillRoundRect(0, 0, width, height / 4, radius, radius);
        }

        // Glow border (if enabled)
        if (ThemeColors.HAS_GLOW) {
            g2.setColor(ThemeColors.withAlpha(accentColor, 40));
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(1, 1, width - 3, height - 3, radius - 1, radius - 1);
        } else {
            // Simple border
            g2.setColor(ThemeColors.BORDER_DEFAULT);
            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(0, 0, width - 1, height - 1, radius, radius);
        }

        // Accent bar at top
        g2.setColor(accentColor);
        g2.fillRoundRect(padding, padding / 2, 50, 4, 2, 2);

        // Icon
        g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        g2.setColor(accentColor);
        g2.drawString(icon, padding, padding + 30);

        // Value
        g2.setFont(ThemeColors.FONT_CARD_VALUE);
        g2.setColor(ThemeColors.TEXT_PRIMARY);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(value, padding, height - padding - fm.getDescent() - 16);

        // Label
        g2.setFont(ThemeColors.FONT_CARD_LABEL);
        g2.setColor(ThemeColors.TEXT_SECONDARY);
        g2.drawString(label, padding, height - padding);

        g2.dispose();
    }
}
