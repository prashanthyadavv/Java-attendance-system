package attendance.ui.components;

import attendance.ui.theme.ThemeColors;

import javax.swing.*;
import java.awt.*;

/**
 * Theme-aware gradient panel with optional glassmorphism effect.
 */
public class GradientPanel extends JPanel {
    private boolean isCard;

    public GradientPanel() {
        this(false);
    }

    public GradientPanel(boolean isCard) {
        this.isCard = isCard;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int radius = isCard ? ThemeColors.BORDER_RADIUS_MEDIUM : ThemeColors.BORDER_RADIUS_SMALL;

        if (isCard) {
            // Shadow (if enabled)
            if (ThemeColors.HAS_SHADOWS) {
                g2.setColor(ThemeColors.SHADOW_COLOR);
                g2.fillRoundRect(
                        ThemeColors.SHADOW_OFFSET_X,
                        ThemeColors.SHADOW_OFFSET_Y + 2,
                        width - 2, height - 2, radius, radius);
            }

            // Card background with gradient
            if (ThemeColors.HAS_GRADIENTS) {
                GradientPaint gradient = new GradientPaint(
                        0, 0, ThemeColors.BG_CARD,
                        0, height, ThemeColors.withAlpha(ThemeColors.BG_LIGHT, 200));
                g2.setPaint(gradient);
            } else {
                g2.setColor(ThemeColors.BG_CARD);
            }
            g2.fillRoundRect(0, 0, width, height, radius, radius);

            // Glassmorphism effect (if enabled)
            if (ThemeColors.HAS_GLASSMORPHISM) {
                g2.setColor(ThemeColors.withAlpha(Color.WHITE, (int) (ThemeColors.GLASS_OPACITY * 255)));
                g2.fillRoundRect(0, 0, width, height / 3, radius, radius);
            }

            // Border
            Color borderColor = ThemeColors.HAS_GLOW ? ThemeColors.withAlpha(ThemeColors.ACCENT_CYAN, 40)
                    : ThemeColors.BORDER_DEFAULT;
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(0, 0, width - 1, height - 1, radius, radius);

            // Glow border (if enabled)
            if (ThemeColors.HAS_GLOW) {
                g2.setColor(ThemeColors.withAlpha(ThemeColors.ACCENT_CYAN, 20));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, width - 3, height - 3, radius - 1, radius - 1);
            }
        } else {
            // Full panel gradient background
            if (ThemeColors.HAS_GRADIENTS && ThemeColors.IS_DARK_MODE) {
                GradientPaint gradient = new GradientPaint(
                        0, 0, ThemeColors.BG_DARK,
                        width, height, ThemeColors.withAlpha(ThemeColors.BG_MEDIUM, 230));
                g2.setPaint(gradient);
            } else {
                g2.setColor(ThemeColors.BG_DARK);
            }
            g2.fillRect(0, 0, width, height);
        }

        g2.dispose();
    }
}
