package attendance.ui.components;

import attendance.ui.theme.ThemeColors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * Theme-aware sidebar panel with navigation items.
 */
public class SidebarPanel extends JPanel {
    private JPanel headerPanel;
    private JPanel itemsPanel;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private List<SidebarItem> items = new ArrayList<>();
    private ActionListener navigationListener;
    private int selectedIndex = 0;

    public SidebarPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(ThemeColors.SIDEBAR_WIDTH, 0));
        setBackground(ThemeColors.BG_SIDEBAR);

        initHeader();
        initItemsPanel();
        initFooter();
    }

    private void initHeader() {
        headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(
                ThemeColors.SPACING_LARGE,
                ThemeColors.PADDING_INPUT,
                ThemeColors.SPACING_LARGE,
                ThemeColors.PADDING_INPUT));

        titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(ThemeColors.FONT_SUBTITLE);
        titleLabel.setForeground(ThemeColors.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.add(titleLabel);

        headerPanel.add(Box.createVerticalStrut(ThemeColors.SPACING_SMALL));

        subtitleLabel = new JLabel("Welcome");
        subtitleLabel.setFont(ThemeColors.FONT_SMALL);
        subtitleLabel.setForeground(ThemeColors.TEXT_MUTED);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.add(subtitleLabel);

        add(headerPanel, BorderLayout.NORTH);
    }

    private void initItemsPanel() {
        itemsPanel = new JPanel();
        itemsPanel.setOpaque(false);
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setBorder(BorderFactory.createEmptyBorder(
                ThemeColors.SPACING_MEDIUM, 0,
                ThemeColors.SPACING_MEDIUM, 0));

        add(itemsPanel, BorderLayout.CENTER);
    }

    private void initFooter() {
        JPanel footer = new JPanel(
                new FlowLayout(FlowLayout.LEFT, ThemeColors.SPACING_SMALL, ThemeColors.SPACING_SMALL));
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createEmptyBorder(
                ThemeColors.SPACING_MEDIUM,
                ThemeColors.PADDING_INPUT,
                ThemeColors.SPACING_MEDIUM,
                ThemeColors.PADDING_INPUT));

        GlowButton logoutBtn = new GlowButton("Logout", ThemeColors.STATUS_DANGER);
        logoutBtn.setPreferredSize(
                new Dimension(ThemeColors.SIDEBAR_WIDTH - ThemeColors.PADDING_INPUT * 2, ThemeColors.BUTTON_HEIGHT));
        logoutBtn.addActionListener(e -> {
            if (navigationListener != null) {
                navigationListener.actionPerformed(new ActionEvent(this, 0, "LOGOUT"));
            }
        });
        footer.add(logoutBtn);

        add(footer, BorderLayout.SOUTH);
    }

    public void setHeader(String title, String subtitle) {
        titleLabel.setText(title);
        subtitleLabel.setText(subtitle);
    }

    public void addItem(String icon, String text, String actionCommand) {
        SidebarItem item = new SidebarItem(icon, text, actionCommand, items.size());
        items.add(item);
        itemsPanel.add(item);

        if (items.size() == 1) {
            item.setSelected(true);
        }
    }

    public void setNavigationListener(ActionListener listener) {
        this.navigationListener = listener;
    }

    private void selectItem(int index) {
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setSelected(i == index);
        }
        selectedIndex = index;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Sidebar background with gradient
        if (ThemeColors.HAS_GRADIENTS && ThemeColors.IS_DARK_MODE) {
            GradientPaint gradient = new GradientPaint(
                    0, 0, ThemeColors.BG_SIDEBAR,
                    width, 0, ThemeColors.withAlpha(ThemeColors.BG_SIDEBAR, 200));
            g2.setPaint(gradient);
        } else {
            g2.setColor(ThemeColors.BG_SIDEBAR);
        }
        g2.fillRect(0, 0, width, height);

        // Right border
        g2.setColor(ThemeColors.withAlpha(ThemeColors.BORDER_DEFAULT, 50));
        g2.drawLine(width - 1, 0, width - 1, height);

        g2.dispose();
    }

    private class SidebarItem extends JPanel {
        private String icon;
        private String text;
        private String actionCommand;
        private int index;
        private boolean selected = false;
        private boolean hovered = false;

        public SidebarItem(String icon, String text, String actionCommand, int index) {
            this.icon = icon;
            this.text = text;
            this.actionCommand = actionCommand;
            this.index = index;

            setOpaque(false);
            setPreferredSize(new Dimension(ThemeColors.SIDEBAR_WIDTH, ThemeColors.SIDEBAR_ITEM_HEIGHT));
            setMaximumSize(new Dimension(ThemeColors.SIDEBAR_WIDTH, ThemeColors.SIDEBAR_ITEM_HEIGHT));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    repaint();
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    selectItem(SidebarItem.this.index);
                    if (navigationListener != null) {
                        navigationListener.actionPerformed(new ActionEvent(this, 0, SidebarItem.this.actionCommand));
                    }
                }
            });
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int padding = ThemeColors.PADDING_INPUT;
            int radius = ThemeColors.BORDER_RADIUS_SMALL;

            // Background
            if (selected) {
                if (ThemeColors.HAS_GLOW) {
                    // Glow indicator for selected
                    g2.setColor(ThemeColors.withAlpha(ThemeColors.ACCENT_CYAN, 30));
                    g2.fillRoundRect(padding / 2, 3, width - padding, height - 6, radius, radius);
                } else {
                    g2.setColor(ThemeColors.withAlpha(ThemeColors.ACCENT_CYAN, 40));
                    g2.fillRoundRect(padding / 2, 3, width - padding, height - 6, radius, radius);
                }

                // Left accent bar
                g2.setColor(ThemeColors.ACCENT_CYAN);
                g2.fillRoundRect(0, 8, 4, height - 16, 2, 2);
            } else if (hovered) {
                g2.setColor(ThemeColors.withAlpha(Color.WHITE, 10));
                g2.fillRoundRect(padding / 2, 3, width - padding, height - 6, radius, radius);
            }

            // Icon
            g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
            g2.setColor(selected ? ThemeColors.ACCENT_CYAN : ThemeColors.TEXT_SECONDARY);
            g2.drawString(icon, padding + 8, height / 2 + 6);

            // Text
            g2.setFont(ThemeColors.FONT_REGULAR);
            g2.setColor(selected ? ThemeColors.TEXT_PRIMARY : ThemeColors.TEXT_SECONDARY);
            g2.drawString(text, padding + 40, height / 2 + 5);

            g2.dispose();
        }
    }
}
