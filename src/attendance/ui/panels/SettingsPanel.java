package attendance.ui.panels;

import attendance.ui.theme.*;
import attendance.ui.components.*;

import javax.swing.*;
import java.awt.*;

/**
 * Settings panel with SEPARATE controls for:
 * - Theme (visual identity: fonts, spacing, effects)
 * - Mode (light/dark brightness)
 */
public class SettingsPanel extends JPanel implements ThemeManager.ThemeChangeListener, ModeManager.ModeChangeListener {
    private ThemeManager themeManager;
    private ModeManager modeManager;
    private JFrame parentFrame;

    public SettingsPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.themeManager = ThemeManager.getInstance();
        this.modeManager = ModeManager.getInstance();

        themeManager.addThemeChangeListener(this);
        modeManager.addModeChangeListener(this);

        initializeUI();
    }

    private void initializeUI() {
        setBackground(ThemeColors.BG_DARK);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(
                ThemeColors.SPACING_LARGE, ThemeColors.SPACING_LARGE,
                ThemeColors.SPACING_LARGE, ThemeColors.SPACING_LARGE));

        // Header
        JLabel headerLabel = new JLabel("Settings & Appearance");
        headerLabel.setFont(ThemeColors.FONT_TITLE);
        headerLabel.setForeground(ThemeColors.TEXT_PRIMARY);
        add(headerLabel, BorderLayout.NORTH);

        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(ThemeColors.SPACING_LARGE, 0, 0, 0));

        // === LIGHT/DARK MODE SECTION ===
        contentPanel.add(createModeSection());
        contentPanel.add(Box.createVerticalStrut(ThemeColors.SPACING_LARGE));

        // === THEME SELECTION SECTION ===
        contentPanel.add(createThemeSection());
        contentPanel.add(Box.createVerticalStrut(ThemeColors.SPACING_LARGE));

        // === CURRENT SETTINGS INFO ===
        contentPanel.add(createInfoSection());

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * MODE SECTION - Light/Dark toggle
     */
    private JPanel createModeSection() {
        JPanel section = new JPanel();
        section.setOpaque(false);
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Title
        JLabel title = new JLabel("Display Mode");
        title.setFont(ThemeColors.FONT_SUBTITLE);
        title.setForeground(ThemeColors.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(title);

        section.add(Box.createVerticalStrut(4));

        JLabel desc = new JLabel("Adjust brightness for eye comfort (does not change UI personality)");
        desc.setFont(ThemeColors.FONT_SMALL);
        desc.setForeground(ThemeColors.TEXT_MUTED);
        desc.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(desc);

        section.add(Box.createVerticalStrut(ThemeColors.SPACING_MEDIUM));

        // Mode buttons
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, ThemeColors.GAP_MEDIUM, 0));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Dark mode button
        JPanel darkCard = createModeCard("Dark Mode", "Reduced eye strain in low light",
                "\uD83C\uDF19", modeManager.isDarkMode(), () -> {
                    modeManager.setDarkMode();
                    restartToApply();
                });
        buttonsPanel.add(darkCard);

        // Light mode button
        JPanel lightCard = createModeCard("Light Mode", "Standard viewing conditions",
                "\u2600\uFE0F", !modeManager.isDarkMode(), () -> {
                    modeManager.setLightMode();
                    restartToApply();
                });
        buttonsPanel.add(lightCard);

        section.add(buttonsPanel);

        return section;
    }

    /**
     * THEME SECTION - Visual identity selection
     */
    private JPanel createThemeSection() {
        JPanel section = new JPanel();
        section.setOpaque(false);
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Title
        JLabel title = new JLabel("Theme Style");
        title.setFont(ThemeColors.FONT_SUBTITLE);
        title.setForeground(ThemeColors.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(title);

        section.add(Box.createVerticalStrut(4));

        JLabel desc = new JLabel("Change fonts, spacing, shapes, and effects (visual personality)");
        desc.setFont(ThemeColors.FONT_SMALL);
        desc.setForeground(ThemeColors.TEXT_MUTED);
        desc.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(desc);

        section.add(Box.createVerticalStrut(ThemeColors.SPACING_MEDIUM));

        // Theme cards
        JPanel themesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, ThemeColors.GAP_LARGE, ThemeColors.GAP_MEDIUM));
        themesPanel.setOpaque(false);
        themesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (String themeName : themeManager.getAvailableThemeNames()) {
            Theme t = themeManager.getTheme(themeName);
            boolean isSelected = t.getName().equals(themeManager.getCurrentTheme().getName());
            themesPanel.add(createThemeCard(t, isSelected));
        }

        section.add(themesPanel);

        return section;
    }

    private JPanel createModeCard(String title, String desc, String icon, boolean isSelected, Runnable onClick) {
        int radius = ThemeColors.BORDER_RADIUS_MEDIUM;

        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(ThemeColors.BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

                if (isSelected) {
                    g2.setColor(ThemeColors.ACCENT_CYAN);
                    g2.setStroke(new BasicStroke(3));
                } else {
                    g2.setColor(ThemeColors.BORDER_DEFAULT);
                    g2.setStroke(new BasicStroke(1));
                }
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, radius, radius);

                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(180, 120));
        card.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onClick.run();
            }
        });

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(iconLabel);

        card.add(Box.createVerticalStrut(8));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(ThemeColors.FONT_BUTTON);
        titleLabel.setForeground(isSelected ? ThemeColors.ACCENT_CYAN : ThemeColors.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(titleLabel);

        JLabel descLabel = new JLabel("<html>" + desc + "</html>");
        descLabel.setFont(ThemeColors.FONT_SMALL);
        descLabel.setForeground(ThemeColors.TEXT_MUTED);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(descLabel);

        return card;
    }

    private JPanel createThemeCard(Theme theme, boolean isSelected) {
        int radius = theme.getBorderRadiusMedium();

        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(ThemeColors.BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

                if (isSelected) {
                    g2.setColor(ThemeColors.ACCENT_CYAN);
                    g2.setStroke(new BasicStroke(3));
                } else {
                    g2.setColor(ThemeColors.BORDER_DEFAULT);
                    g2.setStroke(new BasicStroke(1));
                }
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, radius, radius);

                g2.dispose();
            }
        };
        card.setLayout(new BorderLayout());
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(220, 240));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                selectTheme(theme.getName());
            }
        });

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(theme.getPaddingCard(), theme.getPaddingCard(),
                theme.getPaddingCard(), theme.getPaddingCard()));

        // Theme name
        JLabel nameLabel = new JLabel(theme.getName());
        nameLabel.setFont(theme.getFontSubtitle());
        nameLabel.setForeground(ThemeColors.TEXT_PRIMARY);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(nameLabel);

        content.add(Box.createVerticalStrut(4));

        // Description
        JLabel descLabel = new JLabel("<html><body style='width: 150px'>" + theme.getDescription() + "</body></html>");
        descLabel.setFont(theme.getFontBodySmall());
        descLabel.setForeground(ThemeColors.TEXT_MUTED);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(descLabel);

        content.add(Box.createVerticalStrut(12));

        // Mini preview
        content.add(createThemePreview(theme));

        content.add(Box.createVerticalGlue());

        // Status
        JLabel statusLabel = new JLabel(isSelected ? "CURRENT" : "Click to Apply");
        statusLabel.setFont(theme.getFontBodySmall());
        statusLabel.setForeground(isSelected ? ThemeColors.ACCENT_CYAN : ThemeColors.TEXT_MUTED);
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(statusLabel);

        card.add(content, BorderLayout.CENTER);

        return card;
    }

    private JPanel createThemePreview(Theme theme) {
        JPanel preview = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();
                int r = theme.getBorderRadiusSmall();

                // Background
                g2.setColor(ThemeColors.BG_MEDIUM);
                g2.fillRoundRect(0, 0, w, h, r, r);

                // Sample button
                g2.setColor(ThemeColors.ACCENT_CYAN);
                g2.fillRoundRect(8, 8, 50, 16, theme.getBorderRadiusSmall(), theme.getBorderRadiusSmall());

                // Sample card
                g2.setColor(ThemeColors.BG_CARD);
                g2.fillRoundRect(8, 30, w - 16, 30, theme.getBorderRadiusMedium(), theme.getBorderRadiusMedium());

                // Sample text lines
                g2.setColor(ThemeColors.TEXT_PRIMARY);
                g2.fillRoundRect(16, 38, 40, 6, 2, 2);
                g2.setColor(ThemeColors.TEXT_MUTED);
                g2.fillRoundRect(16, 48, 60, 4, 2, 2);

                // Border
                g2.setColor(ThemeColors.BORDER_DEFAULT);
                g2.drawRoundRect(0, 0, w - 1, h - 1, r, r);

                g2.dispose();
            }
        };
        preview.setOpaque(false);
        preview.setPreferredSize(new Dimension(170, 70));
        preview.setMaximumSize(new Dimension(170, 70));
        preview.setAlignmentX(Component.LEFT_ALIGNMENT);
        return preview;
    }

    private JPanel createInfoSection() {
        JPanel section = new JPanel();
        section.setOpaque(false);
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Current Configuration");
        title.setFont(ThemeColors.FONT_SUBTITLE);
        title.setForeground(ThemeColors.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(title);

        section.add(Box.createVerticalStrut(ThemeColors.SPACING_MEDIUM));

        JPanel infoGrid = new JPanel(new GridLayout(0, 2, ThemeColors.GAP_LARGE, ThemeColors.GAP_SMALL));
        infoGrid.setOpaque(false);
        infoGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoGrid.setMaximumSize(new Dimension(400, 200));

        addInfoRow(infoGrid, "Theme", themeManager.getCurrentTheme().getName());
        addInfoRow(infoGrid, "Mode", modeManager.isDarkMode() ? "Dark" : "Light");
        addInfoRow(infoGrid, "Font", themeManager.getCurrentTheme().getFontFamily());
        addInfoRow(infoGrid, "Border Radius", themeManager.getCurrentTheme().getBorderRadiusMedium() + "px");
        addInfoRow(infoGrid, "Glow Effects", themeManager.getCurrentTheme().hasGlowEffects() ? "On" : "Off");
        addInfoRow(infoGrid, "Button Height", themeManager.getCurrentTheme().getButtonHeight() + "px");

        section.add(infoGrid);

        return section;
    }

    private void addInfoRow(JPanel panel, String label, String value) {
        JLabel labelComp = new JLabel(label + ":");
        labelComp.setFont(ThemeColors.FONT_SMALL);
        labelComp.setForeground(ThemeColors.TEXT_MUTED);
        panel.add(labelComp);

        JLabel valueComp = new JLabel(value);
        valueComp.setFont(ThemeColors.FONT_REGULAR);
        valueComp.setForeground(ThemeColors.TEXT_PRIMARY);
        panel.add(valueComp);
    }

    private void selectTheme(String themeName) {
        if (!themeName.equals(themeManager.getCurrentTheme().getName())) {
            int result = JOptionPane.showConfirmDialog(parentFrame,
                    "Switch to " + themeName + " theme?\n\nThe application will restart to apply changes.",
                    "Change Theme",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                modeManager.updateAccentsForTheme(themeName);
                themeManager.setTheme(themeName);
                restartToApply();
            }
        }
    }

    private void restartToApply() {
        SwingUtilities.invokeLater(() -> {
            parentFrame.dispose();
            attendance.ui.frames.LoginFrame loginFrame = new attendance.ui.frames.LoginFrame();
            loginFrame.setVisible(true);
        });
    }

    @Override
    public void onThemeChanged(Theme newTheme) {
        removeAll();
        initializeUI();
        revalidate();
        repaint();
    }

    @Override
    public void onModeChanged(Mode newMode) {
        removeAll();
        initializeUI();
        revalidate();
        repaint();
    }
}
