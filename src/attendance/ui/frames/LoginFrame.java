package attendance.ui.frames;

import attendance.database.DataStore;
import attendance.models.Role;
import attendance.models.User;
import attendance.services.AuthService;
import attendance.ui.components.*;
import attendance.ui.theme.ThemeColors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

/**
 * Futuristic login screen with animated background
 */
public class LoginFrame extends JFrame {
    private StyledTextField usernameField;
    private StyledPasswordField passwordField;
    private GlowButton loginButton;
    private JLabel messageLabel;
    private AuthService authService;

    // Animation variables
    private Timer animationTimer;
    private float animationPhase = 0;

    public LoginFrame() {
        authService = AuthService.getInstance();
        initializeUI();
        startBackgroundAnimation();
    }

    private void initializeUI() {
        setTitle("University Attendance System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with animated background
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintAnimatedBackground(g);
            }
        };
        mainPanel.setBackground(ThemeColors.BG_DARK);

        // Center panel for login form
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Logo/Title area
        JPanel logoPanel = new JPanel();
        logoPanel.setOpaque(false);
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel iconLabel = new JLabel("\uD83C\uDFEB"); // University emoji
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoPanel.add(iconLabel);

        logoPanel.add(Box.createVerticalStrut(15));

        JLabel titleLabel = new JLabel("ATTENDANCE SYSTEM");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(ThemeColors.ACCENT_CYAN);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoPanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("University Management Portal");
        subtitleLabel.setFont(ThemeColors.FONT_SMALL);
        subtitleLabel.setForeground(ThemeColors.TEXT_MUTED);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoPanel.add(subtitleLabel);

        centerPanel.add(logoPanel);
        centerPanel.add(Box.createVerticalStrut(50));

        // Login form panel (glass effect)
        JPanel formPanel = new GradientPanel(true);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        formPanel.setMaximumSize(new Dimension(350, 320));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel loginTitle = new JLabel("Sign In");
        loginTitle.setFont(ThemeColors.FONT_SUBTITLE);
        loginTitle.setForeground(ThemeColors.TEXT_PRIMARY);
        loginTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(loginTitle);
        formPanel.add(Box.createVerticalStrut(25));

        // Username field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(ThemeColors.FONT_SMALL);
        usernameLabel.setForeground(ThemeColors.TEXT_SECONDARY);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(usernameLabel);
        formPanel.add(Box.createVerticalStrut(5));

        usernameField = new StyledTextField("Enter username");
        usernameField.setMaximumSize(new Dimension(270, 45));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(15));

        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(ThemeColors.FONT_SMALL);
        passwordLabel.setForeground(ThemeColors.TEXT_SECONDARY);
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(5));

        passwordField = new StyledPasswordField("Enter password");
        passwordField.setMaximumSize(new Dimension(270, 45));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(25));

        // Login button
        loginButton = new GlowButton("LOGIN", ThemeColors.ACCENT_CYAN);
        loginButton.setMaximumSize(new Dimension(270, 45));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(e -> performLogin());
        formPanel.add(loginButton);
        formPanel.add(Box.createVerticalStrut(15));

        // Message label
        messageLabel = new JLabel(" ");
        messageLabel.setFont(ThemeColors.FONT_SMALL);
        messageLabel.setForeground(ThemeColors.STATUS_DANGER);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(messageLabel);

        centerPanel.add(formPanel);
        centerPanel.add(Box.createVerticalStrut(30));

        // Demo credentials hint
        JPanel hintPanel = new JPanel();
        hintPanel.setOpaque(false);
        hintPanel.setLayout(new BoxLayout(hintPanel, BoxLayout.Y_AXIS));
        hintPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel hintTitle = new JLabel("Demo Credentials:");
        hintTitle.setFont(ThemeColors.FONT_SMALL);
        hintTitle.setForeground(ThemeColors.TEXT_MUTED);
        hintTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        hintPanel.add(hintTitle);

        JLabel adminHint = new JLabel("Admin: admin / admin123");
        adminHint.setFont(ThemeColors.FONT_SMALL);
        adminHint.setForeground(ThemeColors.TEXT_MUTED);
        adminHint.setAlignmentX(Component.CENTER_ALIGNMENT);
        hintPanel.add(adminHint);

        JLabel teacherHint = new JLabel("Teacher: john.smith / teacher123");
        teacherHint.setFont(ThemeColors.FONT_SMALL);
        teacherHint.setForeground(ThemeColors.TEXT_MUTED);
        teacherHint.setAlignmentX(Component.CENTER_ALIGNMENT);
        hintPanel.add(teacherHint);

        JLabel studentHint = new JLabel("Student: alice.johnson / student123");
        studentHint.setFont(ThemeColors.FONT_SMALL);
        studentHint.setForeground(ThemeColors.TEXT_MUTED);
        studentHint.setAlignmentX(Component.CENTER_ALIGNMENT);
        hintPanel.add(studentHint);

        centerPanel.add(hintPanel);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);

        // Enter key to login
        getRootPane().setDefaultButton(loginButton);

        // Focus on username field
        SwingUtilities.invokeLater(() -> usernameField.requestFocusInWindow());
    }

    private void paintAnimatedBackground(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Dark gradient background
        GradientPaint bgGradient = new GradientPaint(
                0, 0, ThemeColors.BG_DARK,
                0, height, new Color(5, 5, 15));
        g2.setPaint(bgGradient);
        g2.fillRect(0, 0, width, height);

        // Animated glowing orbs
        for (int i = 0; i < 5; i++) {
            float phase = animationPhase + i * 0.5f;
            int x = (int) (width * 0.2 + Math.sin(phase * 0.5) * width * 0.3);
            int y = (int) (height * 0.3 + Math.cos(phase * 0.3 + i) * height * 0.2);
            int size = 100 + i * 30;

            Color orbColor = i % 2 == 0 ? ThemeColors.ACCENT_CYAN : ThemeColors.ACCENT_PURPLE;

            // Draw glow
            for (int j = 10; j > 0; j--) {
                float alpha = 0.02f * (1f - (float) j / 10);
                g2.setColor(ThemeColors.withAlpha(orbColor, (int) (alpha * 255)));
                g2.fillOval(x - size / 2 - j * 5, y - size / 2 - j * 5, size + j * 10, size + j * 10);
            }
        }

        // Grid lines
        g2.setColor(new Color(40, 40, 60, 30));
        g2.setStroke(new BasicStroke(1));
        for (int i = 0; i < width; i += 50) {
            g2.drawLine(i, 0, i, height);
        }
        for (int i = 0; i < height; i += 50) {
            g2.drawLine(0, i, width, i);
        }

        g2.dispose();
    }

    private void startBackgroundAnimation() {
        animationTimer = new Timer(50, e -> {
            animationPhase += 0.02f;
            repaint();
        });
        animationTimer.start();
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please enter username and password");
            messageLabel.setForeground(ThemeColors.STATUS_WARNING);
            return;
        }

        User user = authService.login(username, password);

        if (user != null) {
            messageLabel.setText("Login successful!");
            messageLabel.setForeground(ThemeColors.STATUS_SAFE);

            // Stop animation
            if (animationTimer != null) {
                animationTimer.stop();
            }

            // Open appropriate dashboard
            SwingUtilities.invokeLater(() -> {
                openDashboard(user);
                dispose();
            });
        } else {
            messageLabel.setText("Invalid username or password");
            messageLabel.setForeground(ThemeColors.STATUS_DANGER);
            passwordField.setText("");
        }
    }

    private void openDashboard(User user) {
        JFrame dashboard;

        switch (user.getRole()) {
            case ADMIN:
                dashboard = new AdminDashboard();
                break;
            case TEACHER:
                dashboard = new TeacherDashboard();
                break;
            case STUDENT:
                dashboard = new StudentDashboard();
                break;
            default:
                return;
        }

        dashboard.setVisible(true);
    }
}
