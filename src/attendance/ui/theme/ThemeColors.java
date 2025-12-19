package attendance.ui.theme;

import java.awt.*;
import javax.swing.*;

/**
 * THEME COLORS - Combines Theme (visual identity) + Mode (colors)
 * 
 * This class provides static access to the combined styling from:
 * - ThemeManager (fonts, spacing, effects)
 * - ModeManager (colors, brightness)
 * 
 * Theme + Mode = Final UI Appearance
 */
public class ThemeColors {

    // === COLORS (from Mode) ===
    public static Color BG_DARK;
    public static Color BG_MEDIUM;
    public static Color BG_LIGHT;
    public static Color BG_CARD;
    public static Color BG_TABLE;
    public static Color BG_TABLE_HEADER;
    public static Color BG_SIDEBAR;
    public static Color BG_INPUT;
    public static Color BG_BUTTON;
    public static Color BG_DIALOG;

    public static Color ACCENT_CYAN;
    public static Color ACCENT_PURPLE;
    public static Color ACCENT_BLUE;
    public static Color ACCENT_GREEN;

    public static Color STATUS_SAFE;
    public static Color STATUS_WARNING;
    public static Color STATUS_DANGER;
    public static Color STATUS_INFO;

    public static Color TEXT_PRIMARY;
    public static Color TEXT_SECONDARY;
    public static Color TEXT_MUTED;
    public static Color TEXT_ON_ACCENT;

    public static Color BORDER_DEFAULT;
    public static Color BORDER_FOCUS;
    public static Color BORDER_GLOW;

    public static Color GLOW_COLOR;
    public static Color SHADOW_COLOR;

    public static Color GRADIENT_START;
    public static Color GRADIENT_END;

    // === FONTS (from Theme) ===
    public static Font FONT_TITLE;
    public static Font FONT_SUBTITLE;
    public static Font FONT_HEADING;
    public static Font FONT_REGULAR;
    public static Font FONT_SMALL;
    public static Font FONT_BUTTON;
    public static Font FONT_INPUT;
    public static Font FONT_TABLE;
    public static Font FONT_TABLE_HEADER;
    public static Font FONT_CARD_VALUE;
    public static Font FONT_CARD_LABEL;

    // === DIMENSIONS (from Theme) ===
    public static int BORDER_RADIUS_SMALL;
    public static int BORDER_RADIUS_MEDIUM;
    public static int BORDER_RADIUS_LARGE;
    public static int BORDER_RADIUS_ROUND;

    public static int SPACING_SMALL;
    public static int SPACING_MEDIUM;
    public static int SPACING_LARGE;

    public static int PADDING_CARD;
    public static int PADDING_BUTTON;
    public static int PADDING_INPUT;

    public static int GAP_SMALL;
    public static int GAP_MEDIUM;
    public static int GAP_LARGE;

    // === COMPONENT SIZES (from Theme) ===
    public static int BUTTON_HEIGHT;
    public static int INPUT_HEIGHT;
    public static int TABLE_ROW_HEIGHT;
    public static int TABLE_HEADER_HEIGHT;
    public static int SIDEBAR_WIDTH;
    public static int SIDEBAR_ITEM_HEIGHT;
    public static int CARD_MIN_WIDTH;
    public static int CARD_MIN_HEIGHT;

    // === EFFECTS (from Theme) ===
    public static boolean HAS_GLOW;
    public static boolean HAS_GRADIENTS;
    public static boolean HAS_SHADOWS;
    public static boolean HAS_ANIMATIONS;
    public static boolean HAS_GLASSMORPHISM;

    public static int GLOW_RADIUS;
    public static float GLOW_INTENSITY;

    public static int SHADOW_OFFSET_X;
    public static int SHADOW_OFFSET_Y;
    public static int SHADOW_BLUR;
    public static float SHADOW_OPACITY;

    public static float GLASS_OPACITY;

    // === MODE STATE ===
    public static boolean IS_DARK_MODE;

    // Initialize on class load
    static {
        updateStyles();
    }

    /**
     * Update all styles from Theme + Mode
     */
    public static void updateStyles() {
        Theme theme = ThemeManager.getInstance().getCurrentTheme();
        Mode mode = ModeManager.getInstance().getCurrentMode();

        // === COLORS FROM MODE ===
        BG_DARK = mode.getBackgroundPrimary();
        BG_MEDIUM = mode.getBackgroundSecondary();
        BG_LIGHT = mode.getBackgroundTertiary();
        BG_CARD = mode.getBackgroundCard();
        BG_TABLE = mode.getBackgroundTable();
        BG_TABLE_HEADER = mode.getBackgroundTableHeader();
        BG_SIDEBAR = mode.getBackgroundSidebar();
        BG_INPUT = mode.getBackgroundInput();
        BG_BUTTON = mode.getBackgroundButton();
        BG_DIALOG = mode.getBackgroundDialog();

        ACCENT_CYAN = mode.getAccentPrimary();
        ACCENT_PURPLE = mode.getAccentSecondary();
        ACCENT_BLUE = mode.getAccentTertiary();
        ACCENT_GREEN = mode.getStatusSuccess();

        STATUS_SAFE = mode.getStatusSuccess();
        STATUS_WARNING = mode.getStatusWarning();
        STATUS_DANGER = mode.getStatusDanger();
        STATUS_INFO = mode.getStatusInfo();

        TEXT_PRIMARY = mode.getTextPrimary();
        TEXT_SECONDARY = mode.getTextSecondary();
        TEXT_MUTED = mode.getTextMuted();
        TEXT_ON_ACCENT = mode.getTextOnAccent();

        BORDER_DEFAULT = mode.getBorderDefault();
        BORDER_FOCUS = mode.getBorderFocus();
        BORDER_GLOW = mode.getGlowColor();

        GLOW_COLOR = mode.getGlowColor();
        SHADOW_COLOR = mode.getShadowColor();

        GRADIENT_START = mode.getAccentPrimary();
        GRADIENT_END = mode.getAccentSecondary();

        IS_DARK_MODE = mode.isDark();

        // === STYLES FROM THEME ===
        FONT_TITLE = theme.getFontTitle();
        FONT_SUBTITLE = theme.getFontSubtitle();
        FONT_HEADING = theme.getFontHeading();
        FONT_REGULAR = theme.getFontBody();
        FONT_SMALL = theme.getFontBodySmall();
        FONT_BUTTON = theme.getFontButton();
        FONT_INPUT = theme.getFontInput();
        FONT_TABLE = theme.getFontTable();
        FONT_TABLE_HEADER = theme.getFontTableHeader();
        FONT_CARD_VALUE = theme.getFontCardValue();
        FONT_CARD_LABEL = theme.getFontCardLabel();

        BORDER_RADIUS_SMALL = theme.getBorderRadiusSmall();
        BORDER_RADIUS_MEDIUM = theme.getBorderRadiusMedium();
        BORDER_RADIUS_LARGE = theme.getBorderRadiusLarge();
        BORDER_RADIUS_ROUND = theme.getBorderRadiusRound();

        SPACING_SMALL = theme.getSpacingSmall();
        SPACING_MEDIUM = theme.getSpacingMedium();
        SPACING_LARGE = theme.getSpacingLarge();

        PADDING_CARD = theme.getPaddingCard();
        PADDING_BUTTON = theme.getPaddingButton();
        PADDING_INPUT = theme.getPaddingInput();

        GAP_SMALL = theme.getGapSmall();
        GAP_MEDIUM = theme.getGapMedium();
        GAP_LARGE = theme.getGapLarge();

        BUTTON_HEIGHT = theme.getButtonHeight();
        INPUT_HEIGHT = theme.getInputHeight();
        TABLE_ROW_HEIGHT = theme.getTableRowHeight();
        TABLE_HEADER_HEIGHT = theme.getTableHeaderHeight();
        SIDEBAR_WIDTH = theme.getSidebarWidth();
        SIDEBAR_ITEM_HEIGHT = theme.getSidebarItemHeight();
        CARD_MIN_WIDTH = theme.getCardMinWidth();
        CARD_MIN_HEIGHT = theme.getCardMinHeight();

        HAS_GLOW = theme.hasGlowEffects();
        HAS_GRADIENTS = theme.hasGradients();
        HAS_SHADOWS = theme.hasShadows();
        HAS_ANIMATIONS = theme.hasAnimations();
        HAS_GLASSMORPHISM = theme.hasGlassmorphism();

        GLOW_RADIUS = theme.getGlowRadius();
        GLOW_INTENSITY = theme.getGlowIntensity();

        SHADOW_OFFSET_X = theme.getShadowOffsetX();
        SHADOW_OFFSET_Y = theme.getShadowOffsetY();
        SHADOW_BLUR = theme.getShadowBlur();
        SHADOW_OPACITY = theme.getShadowOpacity();

        GLASS_OPACITY = theme.getGlassOpacity();

        // Update UIManager defaults
        applyToUIManager(theme, mode);
    }

    /**
     * Apply styles to UIManager for Swing components
     */
    private static void applyToUIManager(Theme theme, Mode mode) {
        UIManager.put("Panel.background", mode.getBackgroundPrimary());
        UIManager.put("Panel.foreground", mode.getTextPrimary());

        UIManager.put("Label.foreground", mode.getTextPrimary());
        UIManager.put("Label.font", theme.getFontBody());

        UIManager.put("Button.background", mode.getBackgroundButton());
        UIManager.put("Button.foreground", mode.getTextOnAccent());
        UIManager.put("Button.font", theme.getFontButton());

        UIManager.put("TextField.background", mode.getBackgroundInput());
        UIManager.put("TextField.foreground", mode.getTextPrimary());
        UIManager.put("TextField.caretForeground", mode.getAccentPrimary());
        UIManager.put("TextField.font", theme.getFontInput());

        UIManager.put("PasswordField.background", mode.getBackgroundInput());
        UIManager.put("PasswordField.foreground", mode.getTextPrimary());
        UIManager.put("PasswordField.font", theme.getFontInput());

        UIManager.put("ComboBox.background", mode.getBackgroundInput());
        UIManager.put("ComboBox.foreground", mode.getTextPrimary());
        UIManager.put("ComboBox.selectionBackground", mode.getAccentPrimary());
        UIManager.put("ComboBox.selectionForeground", mode.getTextOnAccent());

        UIManager.put("Table.background", mode.getBackgroundTable());
        UIManager.put("Table.foreground", mode.getTextPrimary());
        UIManager.put("Table.font", theme.getFontTable());
        UIManager.put("Table.selectionBackground", withAlpha(mode.getAccentPrimary(), 80));
        UIManager.put("TableHeader.background", mode.getBackgroundTableHeader());
        UIManager.put("TableHeader.foreground", mode.getTextSecondary());
        UIManager.put("TableHeader.font", theme.getFontTableHeader());

        UIManager.put("ScrollPane.background", mode.getBackgroundPrimary());
        UIManager.put("Viewport.background", mode.getBackgroundPrimary());

        UIManager.put("OptionPane.background", mode.getBackgroundDialog());
        UIManager.put("OptionPane.messageForeground", mode.getTextPrimary());
        UIManager.put("OptionPane.messageFont", theme.getFontBody());

        UIManager.put("List.background", mode.getBackgroundTable());
        UIManager.put("List.foreground", mode.getTextPrimary());
        UIManager.put("List.selectionBackground", mode.getAccentPrimary());

        UIManager.put("ProgressBar.background", mode.getBackgroundTertiary());
        UIManager.put("ProgressBar.foreground", mode.getAccentPrimary());
    }

    /**
     * Get current theme
     */
    public static Theme getTheme() {
        return ThemeManager.getInstance().getCurrentTheme();
    }

    /**
     * Get current mode
     */
    public static Mode getMode() {
        return ModeManager.getInstance().getCurrentMode();
    }

    /**
     * Create color with alpha
     */
    public static Color withAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    /**
     * Create gradient paint
     */
    public static GradientPaint createGradient(int x1, int y1, int x2, int y2, Color c1, Color c2) {
        return new GradientPaint(x1, y1, c1, x2, y2, c2);
    }
}
