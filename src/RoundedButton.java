package studentregistrationsystem;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

/** Flat, rounded button with four looks: primary, outline, danger, ghost. */
public class RoundedButton extends JButton {

    public enum Kind { PRIMARY, OUTLINE, DANGER, GHOST }

    private final Kind kind;
    private boolean hover;
    private boolean pressed;

    public RoundedButton(String text, Kind kind) {

        super(text);
        this.kind = kind;

        setFont(Theme.font(Font.BOLD, 13));
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(100, 44));

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                pressed = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                pressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pressed = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();
        Theme.quality(g2);

        Color fill = null;
        Color line = null;
        Color text;

        switch (kind) {
            case PRIMARY:
                fill = pressed || hover ? Theme.PRIMARY_DARK : Theme.PRIMARY;
                text = Color.WHITE;
                break;
            case OUTLINE:
                fill = hover ? Theme.PRIMARY_SOFT : Color.WHITE;
                line = Theme.PRIMARY_LINE;
                text = Theme.PRIMARY_DARK;
                break;
            case DANGER:
                fill = hover ? Theme.DANGER_SOFT : Color.WHITE;
                line = Theme.DANGER_LINE;
                text = Theme.DANGER;
                break;
            default:
                fill = hover ? Theme.HOVER : null;
                text = Theme.TEXT_LABEL;
                break;
        }

        int w = getWidth();
        int h = getHeight();

        if (fill != null) {
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, w - 1, h - 1, 12, 12);
        }

        if (line != null) {
            g2.setColor(line);
            g2.drawRoundRect(0, 0, w - 1, h - 1, 12, 12);
        }

        if (isFocusOwner()) {
            g2.setColor(Theme.PRIMARY);
            g2.drawRoundRect(2, 2, w - 5, h - 5, 10, 10);
        }

        g2.setFont(getFont());
        g2.setColor(text);

        FontMetrics fm = g2.getFontMetrics();
        int tx = (w - fm.stringWidth(getText())) / 2;
        int ty = (h - fm.getHeight()) / 2 + fm.getAscent();

        g2.drawString(getText(), tx, ty);
        g2.dispose();
    }
}