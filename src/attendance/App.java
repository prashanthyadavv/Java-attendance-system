package attendance;

import attendance.database.DataStore;
import attendance.ui.frames.LoginFrame;
import attendance.ui.theme.ThemeColors;

import javax.swing.*;
import java.awt.*;

/**
 * Main entry point for University Attendance Management System
 * 
 * A comprehensive desktop application for managing university attendance
 * with role-based access for Admin, Teacher, and Student users.
 * 
 * Features:
 * - Futuristic dark theme UI with neon accents
 * - Role-based dashboards (Admin, Teacher, Student)
 * - Attendance marking and tracking
 * - Detention threshold management
 * - Visual attendance indicators and charts
 * 
 * Demo Credentials:
 * - Admin: admin / admin123
 * - Teacher: john.smith / teacher123
 * - Student: alice.johnson / student123
 * 
 * @author University Attendance System
 * @version 1.0
 */
public class App {
    public static void main(String[] args) {
        // Set system look and feel properties for better rendering
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        // Initialize data store (loads sample data)
        DataStore.getInstance();

        // Run UI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Set default UI font
                setUIFont(ThemeColors.FONT_REGULAR);

                // Apply dark theme defaults
                UIManager.put("Panel.background", ThemeColors.BG_DARK);
                UIManager.put("OptionPane.background", ThemeColors.BG_MEDIUM);
                UIManager.put("OptionPane.messageForeground", ThemeColors.TEXT_PRIMARY);
                UIManager.put("Button.background", ThemeColors.BG_LIGHT);
                UIManager.put("Button.foreground", ThemeColors.TEXT_PRIMARY);
                UIManager.put("TextField.background", ThemeColors.BG_LIGHT);
                UIManager.put("TextField.foreground", ThemeColors.TEXT_PRIMARY);
                UIManager.put("TextField.caretForeground", ThemeColors.ACCENT_CYAN);
                UIManager.put("ComboBox.background", ThemeColors.BG_LIGHT);
                UIManager.put("ComboBox.foreground", ThemeColors.TEXT_PRIMARY);
                UIManager.put("ComboBox.selectionBackground", ThemeColors.ACCENT_CYAN);
                UIManager.put("ScrollPane.background", ThemeColors.BG_DARK);
                UIManager.put("Viewport.background", ThemeColors.BG_DARK);
                UIManager.put("TabbedPane.background", ThemeColors.BG_MEDIUM);
                UIManager.put("TabbedPane.foreground", ThemeColors.TEXT_PRIMARY);
                UIManager.put("TabbedPane.selected", ThemeColors.BG_LIGHT);
                UIManager.put("Table.background", ThemeColors.BG_MEDIUM);
                UIManager.put("Table.foreground", ThemeColors.TEXT_PRIMARY);
                UIManager.put("Table.selectionBackground", ThemeColors.withAlpha(ThemeColors.ACCENT_CYAN, 80));
                UIManager.put("Table.selectionForeground", ThemeColors.TEXT_PRIMARY);
                UIManager.put("Table.gridColor", ThemeColors.BORDER_DEFAULT);
                UIManager.put("TableHeader.background", ThemeColors.BG_LIGHT);
                UIManager.put("TableHeader.foreground", ThemeColors.TEXT_SECONDARY);
                UIManager.put("Spinner.background", ThemeColors.BG_LIGHT);
                UIManager.put("Spinner.foreground", ThemeColors.TEXT_PRIMARY);
                UIManager.put("ProgressBar.background", ThemeColors.BG_LIGHT);
                UIManager.put("ProgressBar.foreground", ThemeColors.ACCENT_CYAN);

            } catch (Exception e) {
                e.printStackTrace();
            }

            // Create and show login frame
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }

    /**
     * Set the default font for all UI components
     */
    private static void setUIFont(Font font) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, font);
            }
        }
    }
}
