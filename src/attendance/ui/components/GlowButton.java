package attendance.ui.components;

import attendance.ui.theme.ThemeColors;
import attendance.ui.theme.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Theme-aware button component with glow/shadow effects based on theme
 * settings.
 */
public class GlowButton extends JButton {
    private Color baseColor;
    private boolean isHovered = false;
    private float hoverProgress = 0f;
    private Timer hoverTimer;

    public GlowButton(String text, Color color) {
        super(text);
        this.baseColor = color;
        setupButton();
    }

    public GlowButton(String text) {
        this(text, ThemeColors.ACCENT_CYAN);
    }

    private void setupButton() {
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setFont(ThemeColors.FONT_BUTTON);
        setForeground(ThemeColors.TEXT_ON_ACCENT);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        int pad = ThemeColors.PADDING_BUTTON;
        setPreferredSize(new Dimension(140, ThemeColors.BUTTON_HEIGHT));

        // Hover animation
        if (ThemeColors.HAS_ANIMATIONS) {
            hoverTimer = new Timer(16, e -> {
                if (isHovered && hoverProgress < 1f) {
                    hoverProgress = Math.min(1f, hoverProgress + 0.1f);
                    repaint();
                } else if (!isHovered && hoverProgress > 0f) {
                    hoverProgress = Math.max(0f, hoverProgress - 0.1f);
                    repaint();
                } else {
                    hoverTimer.stop();
                }
            });
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                if (hoverTimer != null)
                    hoverTimer.start();
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                if (hoverTimer != null)
                    hoverTimer.start();
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int radius = ThemeColors.BORDER_RADIUS_SMALL;

        // Glow effect (if enabled)
        if (ThemeColors.HAS_GLOW && (isHovered || hoverProgress > 0)) {
            float glowAlpha = ThemeColors.GLOW_INTENSITY * hoverProgress;
            for (int i = ThemeColors.GLOW_RADIUS; i > 0; i -= 3) {
                float alpha = glowAlpha * (1f - (float) i / ThemeColors.GLOW_RADIUS);
                g2.setColor(ThemeColors.withAlpha(baseColor, (int) (alpha * 100)));
                g2.fillRoundRect(-i / 2, -i / 2, width + i, height + i, radius + i, radius + i);
            }
        }

        // Shadow (if enabled and no glow)
        if (ThemeColors.HAS_SHADOWS && !ThemeColors.HAS_GLOW) {
            g2.setColor(ThemeColors.SHADOW_COLOR);
            g2.fillRoundRect(
                    ThemeColors.SHADOW_OFFSET_X,
                    ThemeColors.SHADOW_OFFSET_Y,
                    width, height, radius, radius);
        }

        // Button background
        if (ThemeColors.HAS_GRADIENTS) {
            GradientPaint gradient = new GradientPaint(
                    0, 0, baseColor,
                    0, height, isHovered ? baseColor.brighter() : baseColor.darker());
            g2.setPaint(gradient);
        } else {
            g2.setColor(isHovered ? baseColor.brighter() : baseColor);
        }
        g2.fillRoundRect(0, 0, width, height, radius, radius);

        // Border for professional theme (no glow)
        if (!ThemeColors.HAS_GLOW) {
            g2.setColor(ThemeColors.withAlpha(baseColor.darker(), 100));
            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(0, 0, width - 1, height - 1, radius, radius);
        }

        // Text
        FontMetrics fm = g2.getFontMetrics(getFont());
        int textX = (width - fm.stringWidth(getText())) / 2;
        int textY = (height - fm.getHeight()) / 2 + fm.getAscent();

        g2.setFont(getFont());
        g2.setColor(getForeground());
        g2.drawString(getText(), textX, textY);

        g2.dispose();
    }

    public void setBaseColor(Color color) {
        this.baseColor = color;
        repaint();
    }
}
