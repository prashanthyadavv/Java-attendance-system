package attendance.ui.theme;

import java.awt.*;

/**
 * USER-FRIENDLY THEME - Visual Identity
 * 
 * Soft, accessible appearance with:
 * - Larger spacing for comfort
 * - Rounded buttons
 * - Highly readable fonts (Verdana)
 * - Comfortable, approachable feel
 */
public class UserFriendlyTheme implements Theme {

    private static final String FONT_FAMILY = "Verdana";

    @Override
    public String getName() {
        return "User Friendly";
    }

    @Override
    public String getDescription() {
        return "Soft, accessible design with generous spacing";
    }

    @Override
    public String getFontFamily() {
        return FONT_FAMILY;
    }

    // === FONTS (Large, readable) ===
    @Override
    public Font getFontTitle() {
        return new Font(FONT_FAMILY, Font.BOLD, 30);
    }

    @Override
    public Font getFontSubtitle() {
        return new Font(FONT_FAMILY, Font.BOLD, 20);
    }

    @Override
    public Font getFontHeading() {
        return new Font(FONT_FAMILY, Font.BOLD, 18);
    }

    @Override
    public Font getFontBody() {
        return new Font(FONT_FAMILY, Font.PLAIN, 15);
    }

    @Override
    public Font getFontBodySmall() {
        return new Font(FONT_FAMILY, Font.PLAIN, 13);
    }

    @Override
    public Font getFontButton() {
        return new Font(FONT_FAMILY, Font.BOLD, 15);
    }

    @Override
    public Font getFontInput() {
        return new Font(FONT_FAMILY, Font.PLAIN, 15);
    }

    @Override
    public Font getFontTable() {
        return new Font(FONT_FAMILY, Font.PLAIN, 14);
    }

    @Override
    public Font getFontTableHeader() {
        return new Font(FONT_FAMILY, Font.BOLD, 14);
    }

    @Override
    public Font getFontCardValue() {
        return new Font(FONT_FAMILY, Font.BOLD, 40);
    }

    @Override
    public Font getFontCardLabel() {
        return new Font(FONT_FAMILY, Font.PLAIN, 15);
    }

    // === BORDER RADIUS (Very rounded) ===
    @Override
    public int getBorderRadiusSmall() {
        return 12;
    }

    @Override
    public int getBorderRadiusMedium() {
        return 20;
    }

    @Override
    public int getBorderRadiusLarge() {
        return 28;
    }

    @Override
    public int getBorderRadiusRound() {
        return 999;
    } // Pill shape

    @Override
    public int getBorderWidthDefault() {
        return 2;
    }

    @Override
    public int getBorderWidthFocus() {
        return 3;
    }

    // === SPACING (Generous) ===
    @Override
    public int getSpacingXSmall() {
        return 6;
    }

    @Override
    public int getSpacingSmall() {
        return 12;
    }

    @Override
    public int getSpacingMedium() {
        return 20;
    }

    @Override
    public int getSpacingLarge() {
        return 32;
    }

    @Override
    public int getSpacingXLarge() {
        return 44;
    }

    @Override
    public int getSpacingXXLarge() {
        return 60;
    }

    @Override
    public int getPaddingCard() {
        return 28;
    }

    @Override
    public int getPaddingButton() {
        return 20;
    }

    @Override
    public int getPaddingInput() {
        return 14;
    }

    @Override
    public int getPaddingDialog() {
        return 36;
    }

    @Override
    public int getPaddingSidebar() {
        return 18;
    }

    @Override
    public int getGapSmall() {
        return 12;
    }

    @Override
    public int getGapMedium() {
        return 20;
    }

    @Override
    public int getGapLarge() {
        return 28;
    }

    // === EFFECTS (Subtle) ===
    @Override
    public boolean hasGlowEffects() {
        return false;
    }

    @Override
    public boolean hasGradients() {
        return true;
    } // Soft gradients

    @Override
    public boolean hasShadows() {
        return true;
    }

    @Override
    public boolean hasAnimations() {
        return true;
    } // Smooth transitions

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
        return 4;
    }

    @Override
    public int getShadowBlur() {
        return 16;
    }

    @Override
    public float getShadowOpacity() {
        return 0.2f;
    }

    @Override
    public float getGlassOpacity() {
        return 0f;
    }

    // === COMPONENT SIZES (Large, accessible) ===
    @Override
    public int getButtonHeight() {
        return 48;
    }

    @Override
    public int getButtonHeightSmall() {
        return 38;
    }

    @Override
    public int getButtonHeightLarge() {
        return 56;
    }

    @Override
    public int getInputHeight() {
        return 48;
    }

    @Override
    public int getTableRowHeight() {
        return 52;
    }

    @Override
    public int getTableHeaderHeight() {
        return 54;
    }

    @Override
    public int getSidebarWidth() {
        return 280;
    }

    @Override
    public int getSidebarItemHeight() {
        return 54;
    }

    @Override
    public int getCardMinWidth() {
        return 200;
    }

    @Override
    public int getCardMinHeight() {
        return 160;
    }
}
