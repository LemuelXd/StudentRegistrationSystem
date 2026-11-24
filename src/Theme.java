package studentregistrationsystem;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Central place for the StudentHub colours, fonts and small drawing helpers.
 * Palette: deep teal (primary) + amber and pink for the gender accents.
 */
public final class Theme {

    private Theme() {
    }

    // Brand
    public static final Color PRIMARY = new Color(15, 118, 110);
    public static final Color PRIMARY_DARK = new Color(17, 94, 89);
    public static final Color PRIMARY_SOFT = new Color(227, 244, 241);
    public static final Color PRIMARY_LINE = new Color(153, 213, 205);

    // Surfaces
    public static final Color BACKGROUND = new Color(243, 247, 246);
    public static final Color WHITE = Color.WHITE;
    public static final Color BORDER = new Color(224, 234, 232);
    public static final Color INPUT_BORDER = new Color(203, 213, 225);
    public static final Color ROW_LINE = new Color(238, 242, 241);
    public static final Color ROW_SELECTED = new Color(209, 238, 233);
    public static final Color HEADER_BG = new Color(248, 250, 252);
    public static final Color HOVER = new Color(238, 244, 243);

    // Text
    public static final Color TEXT = new Color(15, 23, 42);
    public static final Color TEXT_SOFT = new Color(51, 65, 85);
    public static final Color TEXT_LABEL = new Color(71, 85, 105);
    public static final Color MUTED = new Color(100, 116, 139);
    public static final Color HINT = new Color(115, 128, 148);

    // Accents
    public static final Color AMBER = new Color(180, 83, 9);
    public static final Color AMBER_SOFT = new Color(255, 251, 235);
    public static final Color PINK = new Color(190, 24, 93);
    public static final Color PINK_SOFT = new Color(253, 242, 248);
    public static final Color SLATE = new Color(71, 85, 105);
    public static final Color SLATE_SOFT = new Color(241, 245, 249);
    public static final Color DANGER = new Color(185, 28, 28);
    public static final Color DANGER_SOFT = new Color(254, 242, 242);
    public static final Color DANGER_LINE = new Color(254, 202, 202);

    public static Font font(int style, int size) {
        return new Font("Segoe UI", style, size);
    }

    public static void quality(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_PURE);
    }

    /** Accent colour used for a student's avatar, matching their gender badge. */
    public static Color genderColor(String gender) {
        if ("Female".equalsIgnoreCase(gender)) {
            return PRIMARY;
        }
        if ("Male".equalsIgnoreCase(gender)) {
            return AMBER;
        }
        return SLATE;
    }

    /** Draws a filled circle with centred initials. */
    public static void drawAvatar(Graphics2D g2, int x, int y,
            int size, String initials, Color color) {

        g2.setColor(color);
        g2.fillOval(x, y, size, size);

        g2.setColor(Color.WHITE);
        g2.setFont(font(Font.BOLD, Math.max(10, size * 2 / 5)));

        FontMetrics fm = g2.getFontMetrics();
        int tx = x + (size - fm.stringWidth(initials)) / 2;
        int ty = y + (size - fm.getHeight()) / 2 + fm.getAscent();

        g2.drawString(initials, tx, ty);
    }

    /** Shortens text with an ellipsis so it fits in maxWidth pixels. */
    public static String fit(String text, FontMetrics fm, int maxWidth) {

        if (fm.stringWidth(text) <= maxWidth) {
            return text;
        }

        String s = text;

        while (s.length() > 1
                && fm.stringWidth(s + "\u2026") > maxWidth) {
            s = s.substring(0, s.length() - 1);
        }

        return s + "\u2026";
    }
}