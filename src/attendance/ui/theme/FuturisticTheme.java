package attendance.ui.theme;

import java.awt.*;

/**
 * FUTURISTIC THEME - Visual Identity
 * 
 * Modern, tech-inspired look with:
 * - Rounded components
 * - Neon/accent highlights (glow effects)
 * - Digital-style fonts (Segoe UI)
 * - Smooth animations
 */
public class FuturisticTheme implements Theme {

    private static final String FONT_FAMILY = "Segoe UI";

    @Override
    public String getName() {
        return "Futuristic";
    }

    @Override
    public String getDescription() {
        return "Modern tech look with glow effects and rounded shapes";
    }

    @Override
    public String getFontFamily() {
        return FONT_FAMILY;
    }

    // === FONTS (Modern, clean) ===
    @Override
    public Font getFontTitle() {
        return new Font(FONT_FAMILY, Font.BOLD, 28);
    }

    @Override
    public Font getFontSubtitle() {
        return new Font(FONT_FAMILY, Font.BOLD, 18);
    }

    @Override
    public Font getFontHeading() {
        return new Font(FONT_FAMILY, Font.BOLD, 16);
    }

    @Override
    public Font getFontBody() {
        return new Font(FONT_FAMILY, Font.PLAIN, 14);
    }

    @Override
    public Font getFontBodySmall() {
        return new Font(FONT_FAMILY, Font.PLAIN, 12);
    }

    @Override
    public Font getFontButton() {
        return new Font(FONT_FAMILY, Font.BOLD, 14);
    }

    @Override
    public Font getFontInput() {
        return new Font(FONT_FAMILY, Font.PLAIN, 14);
    }

    @Override
    public Font getFontTable() {
        return new Font(FONT_FAMILY, Font.PLAIN, 13);
    }

    @Override
    public Font getFontTableHeader() {
        return new Font(FONT_FAMILY, Font.BOLD, 13);
    }

    @Override
    public Font getFontCardValue() {
        return new Font(FONT_FAMILY, Font.BOLD, 36);
    }

    @Override
    public Font getFontCardLabel() {
        return new Font(FONT_FAMILY, Font.PLAIN, 14);
    }

    // === BORDER RADIUS (Rounded) ===
    @Override
    public int getBorderRadiusSmall() {
        return 10;
    }

    @Override
    public int getBorderRadiusMedium() {
        return 16;
    }

    @Override
    public int getBorderRadiusLarge() {
        return 24;
    }

    @Override
    public int getBorderRadiusRound() {
        return 50;
    }

    @Override
    public int getBorderWidthDefault() {
        return 1;
    }

    @Override
    public int getBorderWidthFocus() {
        return 2;
    }

    // === SPACING (Standard) ===
    @Override
    public int getSpacingXSmall() {
        return 4;
    }

    @Override
    public int getSpacingSmall() {
        return 8;
    }

    @Override
    public int getSpacingMedium() {
        return 16;
    }

    @Override
    public int getSpacingLarge() {
        return 24;
    }

    @Override
    public int getSpacingXLarge() {
        return 32;
    }

    @Override
    public int getSpacingXXLarge() {
        return 48;
    }

    @Override
    public int getPaddingCard() {
        return 24;
    }

    @Override
    public int getPaddingButton() {
        return 16;
    }

    @Override
    public int getPaddingInput() {
        return 12;
    }

    @Override
    public int getPaddingDialog() {
        return 32;
    }

    @Override
    public int getPaddingSidebar() {
        return 16;
    }

    @Override
    public int getGapSmall() {
        return 8;
    }

    @Override
    public int getGapMedium() {
        return 16;
    }

    @Override
    public int getGapLarge() {
        return 24;
    }

    // === EFFECTS (Glow enabled) ===
    @Override
    public boolean hasGlowEffects() {
        return true;
    }

    @Override
    public boolean hasGradients() {
        return true;
    }

    @Override
    public boolean hasShadows() {
        return true;
    }

    @Override
    public boolean hasAnimations() {
        return true;
    }

    @Override
    public boolean hasGlassmorphism() {
        return true;
    }

    @Override
    public int getGlowRadius() {
        return 20;
    }

    @Override
    public float getGlowIntensity() {
        return 0.6f;
    }

    @Override
    public int getShadowOffsetX() {
        return 0;
    }

    @Override
    public int getShadowOffsetY() {
        return 8;
    }

    @Override
    public int getShadowBlur() {
        return 24;
    }

    @Override
    public float getShadowOpacity() {
        return 0.4f;
    }

    @Override
    public float getGlassOpacity() {
        return 0.15f;
    }

    // === COMPONENT SIZES ===
    @Override
    public int getButtonHeight() {
        return 42;
    }

    @Override
    public int getButtonHeightSmall() {
        return 32;
    }

    @Override
    public int getButtonHeightLarge() {
        return 50;
    }

    @Override
    public int getInputHeight() {
        return 44;
    }

    @Override
    public int getTableRowHeight() {
        return 48;
    }

    @Override
    public int getTableHeaderHeight() {
        return 50;
    }

    @Override
    public int getSidebarWidth() {
        return 260;
    }

    @Override
    public int getSidebarItemHeight() {
        return 48;
    }

    @Override
    public int getCardMinWidth() {
        return 180;
    }

    @Override
    public int getCardMinHeight() {
        return 140;
    }
}
