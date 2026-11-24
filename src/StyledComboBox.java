package studentregistrationsystem;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

/**
 * Drop-down that matches the HintTextField look: rounded border, teal focus
 * ring, chevron arrow and a muted placeholder (any item starting "Select ").
 */
public class StyledComboBox extends JComboBox<String> {

    public StyledComboBox(String[] items) {

        super(items);

        setUI(new StyledUI());
        setBorder(new RoundBorder());
        setFont(Theme.font(Font.PLAIN, 14));
        setBackground(java.awt.Color.WHITE);
        setForeground(Theme.TEXT);
        setPreferredSize(new Dimension(100, 42));

        setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean selected, boolean focus) {

                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list, value, index, selected, focus);

                String text = String.valueOf(value);

                label.setBorder(new EmptyBorder(0, 12, 0, 12));
                label.setFont(Theme.font(Font.PLAIN, 14));
                label.setPreferredSize(new Dimension(
                        label.getPreferredSize().width, 36));

                if (selected) {
                    label.setBackground(Theme.PRIMARY_SOFT);
                    label.setForeground(Theme.PRIMARY_DARK);
                } else {
                    label.setBackground(java.awt.Color.WHITE);
                    label.setForeground(text.startsWith("Select ")
                            ? Theme.HINT : Theme.TEXT);
                }

                return label;
            }
        });

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

    // Keeps our custom look if the look and feel is changed at runtime.
    @Override
    public void updateUI() {
        setUI(new StyledUI());
    }

    private static class RoundBorder implements Border {

        @Override
        public void paintBorder(Component c, Graphics g,
                int x, int y, int w, int h) {

            Graphics2D g2 = (Graphics2D) g.create();
            Theme.quality(g2);

            boolean focused = c.hasFocus();

            g2.setColor(focused ? Theme.PRIMARY : Theme.INPUT_BORDER);
            g2.setStroke(new BasicStroke(focused ? 2f : 1f));

            int inset = focused ? 1 : 0;
            g2.drawRoundRect(x + inset, y + inset,
                    w - 1 - inset * 2, h - 1 - inset * 2, 12, 12);

            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(2, 2, 2, 2);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }

    private static class StyledUI extends BasicComboBoxUI {

        @Override
        protected JButton createArrowButton() {

            JButton button = new JButton() {

                @Override
                public void paint(Graphics g) {

                    Graphics2D g2 = (Graphics2D) g.create();
                    Theme.quality(g2);

                    g2.setColor(Theme.MUTED);
                    g2.setStroke(new BasicStroke(1.8f,
                            BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                    int cx = getWidth() / 2;
                    int cy = getHeight() / 2;

                    g2.drawPolyline(
                            new int[]{cx - 4, cx, cx + 4},
                            new int[]{cy - 2, cy + 2, cy - 2},
                            3);

                    g2.dispose();
                }
            };

            button.setBorder(new EmptyBorder(0, 0, 0, 0));
            button.setContentAreaFilled(false);
            button.setFocusable(false);
            button.setOpaque(false);

            return button;
        }

        @Override
        public void paintCurrentValueBackground(
                Graphics g, Rectangle bounds, boolean hasFocus) {

            g.setColor(java.awt.Color.WHITE);
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }

        @Override
        public void paintCurrentValue(
                Graphics g, Rectangle bounds, boolean hasFocus) {

            Object selected = comboBox.getSelectedItem();
            String text = selected == null ? "" : selected.toString();

            Graphics2D g2 = (Graphics2D) g.create();
            Theme.quality(g2);

            g2.setFont(Theme.font(Font.PLAIN, 14));
            g2.setColor(text.startsWith("Select ")
                    ? Theme.HINT : Theme.TEXT);

            FontMetrics fm = g2.getFontMetrics();
            int y = bounds.y + (bounds.height - fm.getHeight()) / 2
                    + fm.getAscent();

            g2.drawString(text, bounds.x + 10, y);
            g2.dispose();
        }

        @Override
        protected ComboPopup createPopup() {

            BasicComboPopup popup = new BasicComboPopup(comboBox);
            popup.setBorder(new LineBorder(Theme.INPUT_BORDER));

            return popup;
        }
    }
}