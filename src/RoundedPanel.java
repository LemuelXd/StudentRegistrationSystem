package studentregistrationsystem;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/** A panel with rounded corners, a fill colour and an optional 1px border. */
public class RoundedPanel extends JPanel {

    private final int arc;
    private final Color borderColor;

    public RoundedPanel(int arc, Color fill, Color border) {
        this.arc = arc;
        this.borderColor = border;
        setOpaque(false);
        setBackground(fill);
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();
        Theme.quality(g2);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);

        if (borderColor != null) {
            g2.setColor(borderColor);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
        }

        g2.dispose();
        super.paintComponent(g);
    }
}