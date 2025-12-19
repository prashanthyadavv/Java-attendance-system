package attendance.ui.theme;

import java.util.*;

/**
 * THEME MANAGER - Manages Visual Identity (Theme)
 * 
 * Themes control: fonts, border radius, spacing, shadows, component styles.
 * This is INDEPENDENT from ModeManager which controls colors.
 */
public class ThemeManager {
    private static ThemeManager instance;
    private Theme currentTheme;
    private Map<String, Theme> themes = new LinkedHashMap<>();
    private List<ThemeChangeListener> listeners = new ArrayList<>();

    private ThemeManager() {
        // Register themes
        registerTheme(new FuturisticTheme());
        registerTheme(new ProfessionalTheme());
        registerTheme(new UserFriendlyTheme());

        // Default: Futuristic
        currentTheme = themes.get("Futuristic");
    }

    public static synchronized ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }

    private void registerTheme(Theme theme) {
        themes.put(theme.getName(), theme);
    }

    public Theme getCurrentTheme() {
        return currentTheme;
    }

    public String[] getAvailableThemeNames() {
        return themes.keySet().toArray(new String[0]);
    }

    public Theme getTheme(String name) {
        return themes.get(name);
    }

    /**
     * Switch theme by name
     */
    public void setTheme(String themeName) {
        Theme theme = themes.get(themeName);
        if (theme != null && theme != currentTheme) {
            currentTheme = theme;
            ThemeColors.updateStyles(); // Update combined styles
            notifyListeners();
        }
    }

    public void addThemeChangeListener(ThemeChangeListener listener) {
        listeners.add(listener);
    }

    public void removeThemeChangeListener(ThemeChangeListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (ThemeChangeListener listener : listeners) {
            listener.onThemeChanged(currentTheme);
        }
    }

    public interface ThemeChangeListener {
        void onThemeChanged(Theme newTheme);
    }
}
