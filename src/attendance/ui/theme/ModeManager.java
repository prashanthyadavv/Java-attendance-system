package attendance.ui.theme;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * MODE MANAGER - Manages Color Brightness (Light/Dark)
 * 
 * Mode controls: background colors, text colors, brightness.
 * This is INDEPENDENT from ThemeManager which controls visual identity.
 */
public class ModeManager {
    private static ModeManager instance;
    private Mode currentMode;
    private DarkMode darkMode;
    private LightMode lightMode;
    private List<ModeChangeListener> listeners = new ArrayList<>();

    // Accent color schemes per theme
    private static final Map<String, Color[]> THEME_ACCENTS = new HashMap<>();

    static {
        // Futuristic: Neon cyan/purple
        THEME_ACCENTS.put("Futuristic", new Color[] {
                new Color(0, 220, 255), // Cyan
                new Color(160, 80, 220), // Purple
                new Color(0, 200, 180) // Teal
        });

        // Professional: Corporate blue
        THEME_ACCENTS.put("Professional", new Color[] {
                new Color(37, 99, 235), // Blue
                new Color(79, 70, 229), // Indigo
                new Color(6, 182, 212) // Cyan
        });

        // User Friendly: Warm orange
        THEME_ACCENTS.put("User Friendly", new Color[] {
                new Color(234, 88, 12), // Orange
                new Color(245, 158, 11), // Amber
                new Color(22, 163, 74) // Green
        });
    }

    private ModeManager() {
        darkMode = new DarkMode();
        lightMode = new LightMode();
        currentMode = darkMode; // Default: Dark

        // Set initial accents
        updateAccentsForTheme("Futuristic");
    }

    public static synchronized ModeManager getInstance() {
        if (instance == null) {
            instance = new ModeManager();
        }
        return instance;
    }

    public Mode getCurrentMode() {
        return currentMode;
    }

    public boolean isDarkMode() {
        return currentMode.isDark();
    }

    /**
     * Switch to dark mode
     */
    public void setDarkMode() {
        if (currentMode != darkMode) {
            currentMode = darkMode;
            ThemeColors.updateStyles();
            notifyListeners();
        }
    }

    /**
     * Switch to light mode
     */
    public void setLightMode() {
        if (currentMode != lightMode) {
            currentMode = lightMode;
            ThemeColors.updateStyles();
            notifyListeners();
        }
    }

    /**
     * Toggle between light/dark
     */
    public void toggleMode() {
        if (isDarkMode()) {
            setLightMode();
        } else {
            setDarkMode();
        }
    }

    /**
     * Update accent colors when theme changes
     */
    public void updateAccentsForTheme(String themeName) {
        Color[] accents = THEME_ACCENTS.get(themeName);
        if (accents != null) {
            darkMode.setAccentScheme(accents[0], accents[1], accents[2]);
            lightMode.setAccentScheme(accents[0], accents[1], accents[2]);
        }
    }

    public void addModeChangeListener(ModeChangeListener listener) {
        listeners.add(listener);
    }

    public void removeModeChangeListener(ModeChangeListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (ModeChangeListener listener : listeners) {
            listener.onModeChanged(currentMode);
        }
    }

    public interface ModeChangeListener {
        void onModeChanged(Mode newMode);
    }
}
