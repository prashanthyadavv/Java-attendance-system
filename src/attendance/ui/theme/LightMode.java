package attendance.ui.theme;

import java.awt.*;

/**
 * LIGHT MODE - Color Palette
 * 
 * Light backgrounds, dark text for standard viewing conditions.
 * Colors are adjusted per accent scheme (set by setAccentScheme).
 */
public class LightMode implements Mode {

    private Color accentPrimary = new Color(37, 99, 235);
    private Color accentSecondary = new Color(124, 58, 237);
    private Color accentTertiary = new Color(6, 182, 212);

    @Override
    public String getName() {
        return "Light";
    }

    @Override
    public boolean isDark() {
        return false;
    }

    // === BACKGROUND COLORS (Light) ===
    @Override
    public Color getBackgroundPrimary() {
        return new Color(248, 250, 252);
    }

    @Override
    public Color getBackgroundSecondary() {
        return new Color(255, 255, 255);
    }

    @Override
    public Color getBackgroundTertiary() {
        return new Color(241, 245, 249);
    }

    @Override
    public Color getBackgroundCard() {
        return new Color(255, 255, 255);
    }

    @Override
    public Color getBackgroundTable() {
        return new Color(255, 255, 255);
    }

    @Override
    public Color getBackgroundTableHeader() {
        return new Color(241, 245, 249);
    }

    @Override
    public Color getBackgroundTableRow() {
        return new Color(255, 255, 255);
    }

    @Override
    public Color getBackgroundTableRowAlt() {
        return new Color(249, 250, 251);
    }

    @Override
    public Color getBackgroundSidebar() {
        return new Color(30, 41, 59);
    }

    @Override
    public Color getBackgroundInput() {
        return new Color(255, 255, 255);
    }

    @Override
    public Color getBackgroundButton() {
        return accentPrimary;
    }

    @Override
    public Color getBackgroundButtonHover() {
        return accentPrimary.darker();
    }

    @Override
    public Color getBackgroundDialog() {
        return new Color(255, 255, 255);
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

    // === STATUS COLORS (Slightly darker for light bg) ===
    @Override
    public Color getStatusSuccess() {
        return new Color(22, 163, 74);
    }

    @Override
    public Color getStatusWarning() {
        return new Color(202, 138, 4);
    }

    @Override
    public Color getStatusDanger() {
        return new Color(220, 38, 38);
    }

    @Override
    public Color getStatusInfo() {
        return new Color(37, 99, 235);
    }

    // === TEXT COLORS ===
    @Override
    public Color getTextPrimary() {
        return new Color(15, 23, 42);
    }

    @Override
    public Color getTextSecondary() {
        return new Color(71, 85, 105);
    }

    @Override
    public Color getTextMuted() {
        return new Color(148, 163, 184);
    }

    @Override
    public Color getTextOnAccent() {
        return new Color(255, 255, 255);
    }

    @Override
    public Color getTextLink() {
        return accentPrimary;
    }

    // === BORDER COLORS ===
    @Override
    public Color getBorderDefault() {
        return new Color(226, 232, 240);
    }

    @Override
    public Color getBorderFocus() {
        return accentPrimary;
    }

    @Override
    public Color getBorderCard() {
        return new Color(226, 232, 240);
    }

    @Override
    public Color getBorderInput() {
        return new Color(203, 213, 225);
    }

    // === EFFECT COLORS ===
    @Override
    public Color getGlowColor() {
        return withAlpha(accentPrimary, 80);
    }

    @Override
    public Color getShadowColor() {
        return new Color(0, 0, 0, 30);
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
