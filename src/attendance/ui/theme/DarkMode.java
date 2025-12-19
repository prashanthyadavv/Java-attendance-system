package attendance.ui.theme;

import java.awt.*;

/**
 * DARK MODE - Color Palette
 * 
 * Dark backgrounds, light text for reduced eye strain in low light.
 * Colors are adjusted per accent scheme (set by setAccentScheme).
 */
public class DarkMode implements Mode {

    private Color accentPrimary = new Color(0, 200, 255);
    private Color accentSecondary = new Color(160, 80, 220);
    private Color accentTertiary = new Color(0, 180, 180);

    @Override
    public String getName() {
        return "Dark";
    }

    @Override
    public boolean isDark() {
        return true;
    }

    // === BACKGROUND COLORS (Dark) ===
    @Override
    public Color getBackgroundPrimary() {
        return new Color(16, 18, 24);
    }

    @Override
    public Color getBackgroundSecondary() {
        return new Color(24, 28, 36);
    }

    @Override
    public Color getBackgroundTertiary() {
        return new Color(32, 38, 48);
    }

    @Override
    public Color getBackgroundCard() {
        return new Color(28, 32, 42);
    }

    @Override
    public Color getBackgroundTable() {
        return new Color(20, 24, 32);
    }

    @Override
    public Color getBackgroundTableHeader() {
        return new Color(36, 42, 54);
    }

    @Override
    public Color getBackgroundTableRow() {
        return new Color(24, 28, 38);
    }

    @Override
    public Color getBackgroundTableRowAlt() {
        return new Color(28, 34, 44);
    }

    @Override
    public Color getBackgroundSidebar() {
        return new Color(14, 16, 22);
    }

    @Override
    public Color getBackgroundInput() {
        return new Color(22, 26, 34);
    }

    @Override
    public Color getBackgroundButton() {
        return accentPrimary;
    }

    @Override
    public Color getBackgroundButtonHover() {
        return accentPrimary.brighter();
    }

    @Override
    public Color getBackgroundDialog() {
        return new Color(22, 26, 34);
    }

    // === ACCENT COLORS ===
    @Override
    public Color getAccentPrimary() {
        return accentPrimary;
    }

    @Override
    public Color getAccentSecondary() {
        return accentSecondary;
    }

    @Override
    public Color getAccentTertiary() {
        return accentTertiary;
    }

    // === STATUS COLORS ===
    @Override
    public Color getStatusSuccess() {
        return new Color(34, 197, 94);
    }

    @Override
    public Color getStatusWarning() {
        return new Color(250, 204, 21);
    }

    @Override
    public Color getStatusDanger() {
        return new Color(239, 68, 68);
    }

    @Override
    public Color getStatusInfo() {
        return new Color(59, 130, 246);
    }

    // === TEXT COLORS ===
    @Override
    public Color getTextPrimary() {
        return new Color(248, 250, 252);
    }

    @Override
    public Color getTextSecondary() {
        return new Color(175, 185, 200);
    }

    @Override
    public Color getTextMuted() {
        return new Color(120, 130, 145);
    }

    @Override
    public Color getTextOnAccent() {
        return new Color(16, 18, 24);
    }

    @Override
    public Color getTextLink() {
        return accentPrimary;
    }

    // === BORDER COLORS ===
    @Override
    public Color getBorderDefault() {
        return new Color(55, 65, 80);
    }

    @Override
    public Color getBorderFocus() {
        return accentPrimary;
    }

    @Override
    public Color getBorderCard() {
        return new Color(45, 55, 70);
    }

    @Override
    public Color getBorderInput() {
        return new Color(60, 70, 85);
    }

    // === EFFECT COLORS ===
    @Override
    public Color getGlowColor() {
        return withAlpha(accentPrimary, 120);
    }

    @Override
    public Color getShadowColor() {
        return new Color(0, 0, 0, 100);
    }

    /**
     * Set accent color scheme (called when theme changes)
     */
    public void setAccentScheme(Color primary, Color secondary, Color tertiary) {
        this.accentPrimary = primary;
        this.accentSecondary = secondary;
        this.accentTertiary = tertiary;
    }
}
