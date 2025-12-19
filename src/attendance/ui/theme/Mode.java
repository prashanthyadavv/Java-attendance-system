package attendance.ui.theme;

import java.awt.*;

/**
 * MODE INTERFACE - Defines Color Palette (Light/Dark)
 * 
 * Mode controls: background colors, text colors, border colors, accent colors.
 * Mode does NOT control: fonts, spacing, shadows, component shapes.
 * 
 * Mode is about brightness and eye comfort, not UI personality.
 */
public interface Mode {

    // === IDENTITY ===
    String getName();

    boolean isDark();

    // === BACKGROUND COLORS ===
    Color getBackgroundPrimary(); // Main app background

    Color getBackgroundSecondary(); // Elevated surfaces

    Color getBackgroundTertiary(); // Cards, panels

    Color getBackgroundCard(); // Card background

    Color getBackgroundTable(); // Table background

    Color getBackgroundTableHeader(); // Table header

    Color getBackgroundTableRow(); // Table row

    Color getBackgroundTableRowAlt(); // Alternating row

    Color getBackgroundSidebar(); // Sidebar background

    Color getBackgroundInput(); // Text fields

    Color getBackgroundButton(); // Default button bg

    Color getBackgroundButtonHover(); // Button hover

    Color getBackgroundDialog(); // Dialog background

    // === ACCENT COLORS (Theme-tinted) ===
    Color getAccentPrimary();

    Color getAccentSecondary();

    Color getAccentTertiary();

    // === STATUS COLORS ===
    Color getStatusSuccess();

    Color getStatusWarning();

    Color getStatusDanger();

    Color getStatusInfo();

    // === TEXT COLORS ===
    Color getTextPrimary();

    Color getTextSecondary();

    Color getTextMuted();

    Color getTextOnAccent();

    Color getTextLink();

    // === BORDER COLORS ===
    Color getBorderDefault();

    Color getBorderFocus();

    Color getBorderCard();

    Color getBorderInput();

    // === EFFECT COLORS ===
    Color getGlowColor();

    Color getShadowColor();

    // === UTILITY ===
    default Color withAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
}
