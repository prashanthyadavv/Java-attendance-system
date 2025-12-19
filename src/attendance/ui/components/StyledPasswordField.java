package attendance.ui.components;

import attendance.ui.theme.ThemeColors;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Custom styled password field with neon border
 */
public class StyledPasswordField extends JPasswordField {
    private String placeholder = "";
    private boolean showPlaceholder = true;

    public StyledPasswordField() {
        this(20);
    }

    public StyledPasswordField(int columns) {
        super(columns);
        setupStyle();
    }

    public StyledPasswordField(String placeholder) {
        super(20);
        this.placeholder = placeholder;
        setupStyle();
    }

    private void setupStyle() {
        setFont(ThemeColors.FONT_REGULAR);
        setForeground(ThemeColors.TEXT_PRIMARY);
        setBackground(ThemeColors.BG_LIGHT);
        setCaretColor(ThemeColors.ACCENT_CYAN);
        setEchoChar('\u2022');
        setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(ThemeColors.BORDER_DEFAULT, 8),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        setPreferredSize(new Dimension(250, 40));

        // Focus styling
        addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                setBorder(BorderFactory.createCompoundBorder(
                        new RoundedBorder(ThemeColors.ACCENT_CYAN, 8),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)));
                showPlaceholder = false;
                repaint();
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                setBorder(BorderFactory.createCompoundBorder(
                        new RoundedBorder(ThemeColors.BORDER_DEFAULT, 8),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)));
                showPlaceholder = getPassword().length == 0;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (showPlaceholder && getPassword().length == 0 && !placeholder.isEmpty()) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
            g2.setColor(ThemeColors.TEXT_MUTED);
            g2.setFont(getFont());
            Insets insets = getInsets();
            g2.drawString(placeholder, insets.left, getHeight() / 2 + g2.getFontMetrics().getAscent() / 2 - 2);
            g2.dispose();
        }
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        repaint();
    }

    /**
     * Rounded border implementation
     */
    private static class RoundedBorder implements Border {
        private Color color;
        private int radius;

        RoundedBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(4, 8, 4, 8);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }
}
