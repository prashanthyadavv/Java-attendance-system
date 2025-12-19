package attendance.ui.theme;

import java.awt.*;

/**
 * THEME INTERFACE - Defines Visual Identity (NOT Colors)
 * 
 * Themes control: fonts, border radius, spacing, shadows, component styles.
 * Themes do NOT control: background/text colors (that's Mode's job).
 * 
 * Theme + Mode = Final UI Appearance
 */
public interface Theme {

    // === IDENTITY ===
    String getName();

    String getDescription();

    // === FONTS ===
    String getFontFamily();

    Font getFontTitle(); // Page titles

    Font getFontSubtitle(); // Section headers

    Font getFontHeading(); // Card headings

    Font getFontBody(); // Body text

    Font getFontBodySmall(); // Small body

    Font getFontButton(); // Button text

    Font getFontInput(); // Input text

    Font getFontTable(); // Table text

    Font getFontTableHeader(); // Table header

    Font getFontCardValue(); // Dashboard card values

    Font getFontCardLabel(); // Dashboard card labels

    // === BORDER RADIUS ===
    int getBorderRadiusSmall(); // Inputs, small buttons

    int getBorderRadiusMedium(); // Cards, panels

    int getBorderRadiusLarge(); // Dialogs, large cards

    int getBorderRadiusRound(); // Pills, avatars

    // === BORDER WIDTH ===
    int getBorderWidthDefault();

    int getBorderWidthFocus();

    // === SPACING ===
    int getSpacingXSmall(); // 4px

    int getSpacingSmall(); // 8px

    int getSpacingMedium(); // 16px

    int getSpacingLarge(); // 24px

    int getSpacingXLarge(); // 32px

    int getSpacingXXLarge(); // 48px

    // === PADDING ===
    int getPaddingCard();

    int getPaddingButton();

    int getPaddingInput();

    int getPaddingDialog();

    int getPaddingSidebar();

    // === GAPS ===
    int getGapSmall();

    int getGapMedium();

    int getGapLarge();

    // === EFFECTS ===
    boolean hasGlowEffects(); // Neon glow on buttons/cards

    boolean hasGradients(); // Gradient backgrounds

    boolean hasShadows(); // Drop shadows

    boolean hasAnimations(); // Hover animations

    boolean hasGlassmorphism(); // Glass blur effect

    int getGlowRadius(); // Glow blur radius

    float getGlowIntensity(); // Glow opacity 0-1

    int getShadowOffsetX();

    int getShadowOffsetY();

    int getShadowBlur();

    float getShadowOpacity(); // 0-1

    float getGlassOpacity();

    // === COMPONENT SIZES ===
    int getButtonHeight();

    int getButtonHeightSmall();

    int getButtonHeightLarge();

    int getInputHeight();

    int getTableRowHeight();

    int getTableHeaderHeight();

    int getSidebarWidth();

    int getSidebarItemHeight();

    int getCardMinWidth();

    int getCardMinHeight();

    // === UTILITY ===
    default Insets getCardPadding() {
        int p = getPaddingCard();
        return new Insets(p, p, p, p);
    }

    default Insets getButtonPadding() {
        int p = getPaddingButton();
        return new Insets(p / 2, p, p / 2, p);
    }
}
