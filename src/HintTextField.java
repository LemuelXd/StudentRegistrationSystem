package studentregistrationsystem;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.Icon;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/** Rounded text field with a placeholder, optional leading icon and a focus ring. */
public class HintTextField extends JTextField {

    private final String hint;
    private final Icon icon;

    public HintTextField(String hint) {
        this(hint, null);
    }

    public HintTextField(String hint, Icon icon) {

        this.hint = hint;
        this.icon = icon;

        setOpaque(false);
        setFont(Theme.font(Font.PLAIN, 14));
        setForeground(Theme.TEXT);
        setCaretColor(Theme.PRIMARY);
        setBorder(new EmptyBorder(0, icon != null ? 38 : 14, 0, 14));
        setPreferredSize(new Dimension(100, 42));

        addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();
        Theme.quality(g2);

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
        g2.dispose();

        super.paintComponent(g);

        Graphics2D g3 = (Graphics2D) g.create();
        Theme.quality(g3);

        if (icon != null) {
            icon.paintIcon(this, g3, 12,
                    (getHeight() - icon.getIconHeight()) / 2);
        }

        if (getText().isEmpty() && hint != null) {
            g3.setFont(getFont());
            g3.setColor(Theme.HINT);

            FontMetrics fm = g3.getFontMetrics();
            int x = getInsets().left;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

            g3.drawString(hint, x, y);
        }

        boolean focused = isFocusOwner();

        g3.setColor(focused ? Theme.PRIMARY : Theme.INPUT_BORDER);
        g3.setStroke(new BasicStroke(focused ? 2f : 1f));

        int inset = focused ? 1 : 0;
        g3.drawRoundRect(inset, inset,
                getWidth() - 1 - inset * 2,
                getHeight() - 1 - inset * 2, 12, 12);

        g3.dispose();
    }
}