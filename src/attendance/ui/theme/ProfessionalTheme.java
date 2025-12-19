package attendance.ui.theme;

import java.awt.*;

/**
 * PROFESSIONAL THEME - Visual Identity
 * 
 * Clean, corporate appearance with:
 * - Sharp or minimal rounding
 * - Neutral spacing
 * - No glow effects
 * - Formal fonts (Arial)
 */
public class ProfessionalTheme implements Theme {

    private static final String FONT_FAMILY = "Arial";

    @Override
    public String getName() {
        return "Professional";
    }

    @Override
    public String getDescription() {
        return "Clean corporate look with sharp edges";
    }

    @Override
    public String getFontFamily() {
        return FONT_FAMILY;
    }

    // === FONTS (Formal, compact) ===
    @Override
    public Font getFontTitle() {
        return new Font(FONT_FAMILY, Font.BOLD, 26);
    }

    @Override
    public Font getFontSubtitle() {
        return new Font(FONT_FAMILY, Font.BOLD, 16);
    }

    @Override
    public Font getFontHeading() {
        return new Font(FONT_FAMILY, Font.BOLD, 14);
    }

    @Override
    public Font getFontBody() {
        return new Font(FONT_FAMILY, Font.PLAIN, 13);
    }

    @Override
    public Font getFontBodySmall() {
        return new Font(FONT_FAMILY, Font.PLAIN, 11);
    }

    @Override
    public Font getFontButton() {
        return new Font(FONT_FAMILY, Font.BOLD, 13);
    }

    @Override
    public Font getFontInput() {
        return new Font(FONT_FAMILY, Font.PLAIN, 13);
    }

    @Override
    public Font getFontTable() {
        return new Font(FONT_FAMILY, Font.PLAIN, 12);
    }

    @Override
    public Font getFontTableHeader() {
        return new Font(FONT_FAMILY, Font.BOLD, 12);
    }

    @Override
    public Font getFontCardValue() {
        return new Font(FONT_FAMILY, Font.BOLD, 32);
    }

    @Override
    public Font getFontCardLabel() {
        return new Font(FONT_FAMILY, Font.PLAIN, 12);
    }

    // === BORDER RADIUS (Sharp) ===
    @Override
    public int getBorderRadiusSmall() {
        return 4;
    }

    @Override
    public int getBorderRadiusMedium() {
        return 6;
    }

    @Override
    public int getBorderRadiusLarge() {
        return 8;
    }

    @Override
    public int getBorderRadiusRound() {
        return 20;
    }

    @Override
    public int getBorderWidthDefault() {
        return 1;
    }

    @Override
    public int getBorderWidthFocus() {
        return 2;
    }

    // === SPACING (Compact) ===
    @Override
    public int getSpacingXSmall() {
        return 4;
    }

    @Override
    public int getSpacingSmall() {
        return 6;
    }

    @Override
    public int getSpacingMedium() {
        return 12;
    }

    @Override
    public int getSpacingLarge() {
        return 18;
    }

    @Override
    public int getSpacingXLarge() {
        return 24;
    }

    @Override
    public int getSpacingXXLarge() {
        return 32;
    }

    @Override
    public int getPaddingCard() {
        return 16;
    }

    @Override
    public int getPaddingButton() {
        return 12;
    }

    @Override
    public int getPaddingInput() {
        return 10;
    }

    @Override
    public int getPaddingDialog() {
        return 24;
    }

    @Override
    public int getPaddingSidebar() {
        return 12;
    }

    @Override
    public int getGapSmall() {
        return 6;
    }

    @Override
    public int getGapMedium() {
        return 12;
    }

    @Override
    public int getGapLarge() {
        return 18;
    }

    // === EFFECTS (Minimal) ===
    @Override
    public boolean hasGlowEffects() {
        return false;
    }

    @Override
    public boolean hasGradients() {
        return false;
    }

    @Override
    public boolean hasShadows() {
        return true;
    } // Subtle only

    @Override
    public boolean hasAnimations() {
        return false;
    }

    @Override
    public boolean hasGlassmorphism() {
        return false;
    }

    @Override
    public int getGlowRadius() {
        return 0;
    }

    @Override
    public float getGlowIntensity() {
        return 0f;
    }

    @Override
    public int getShadowOffsetX() {
        return 0;
    }

    @Override
    public int getShadowOffsetY() {
        return 2;
    }

    @Override
    public int getShadowBlur() {
        return 6;
    }

    @Override
    public float getShadowOpacity() {
        return 0.15f;
    }

    @Override
    public float getGlassOpacity() {
        return 0f;
    }

    // === COMPONENT SIZES (Compact) ===
    @Override
    public int getButtonHeight() {
        return 36;
    }

    @Override
    public int getButtonHeightSmall() {
        return 28;
    }

    @Override
    public int getButtonHeightLarge() {
        return 44;
    }

    @Override
    public int getInputHeight() {
        return 36;
    }

    @Override
    public int getTableRowHeight() {
        return 38;
    }

    @Override
    public int getTableHeaderHeight() {
        return 40;
    }

    @Override
    public int getSidebarWidth() {
        return 220;
    }

    @Override
    public int getSidebarItemHeight() {
        return 40;
    }

    @Override
    public int getCardMinWidth() {
        return 160;
    }

    @Override
    public int getCardMinHeight() {
        return 120;
    }
}
