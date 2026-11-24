package studentregistrationsystem;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import javax.swing.Icon;

/**
 * Crisp line icons drawn with Java2D, so the UI no longer depends on
 * Unicode glyphs (which showed up as empty boxes on some systems).
 */
public class VectorIcon implements Icon {

    public enum Type { HOME, USERS, USER, PLUS, SEARCH }

    private final Type type;
    private final int size;
    private final Color color;

    public VectorIcon(Type type, int size, Color color) {
        this.type = type;
        this.size = size;
        this.color = color;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {

        Graphics2D g2 = (Graphics2D) g.create();
        Theme.quality(g2);

        g2.translate(x, y);
        g2.scale(size / 24.0, size / 24.0);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(1.8f,
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        switch (type) {
            case HOME:
                Path2D home = new Path2D.Double();
                home.moveTo(3, 10.5);
                home.lineTo(12, 3);
                home.lineTo(21, 10.5);
                home.lineTo(21, 21);
                home.lineTo(15, 21);
                home.lineTo(15, 15);
                home.lineTo(9, 15);
                home.lineTo(9, 21);
                home.lineTo(3, 21);
                home.closePath();
                g2.draw(home);
                break;

            case USERS:
                g2.draw(new Ellipse2D.Double(5, 3, 8, 8));

                Path2D body = new Path2D.Double();
                body.moveTo(16, 21);
                body.lineTo(16, 19);
                body.curveTo(16, 16.8, 14.2, 15, 12, 15);
                body.lineTo(6, 15);
                body.curveTo(3.8, 15, 2, 16.8, 2, 19);
                body.lineTo(2, 21);
                g2.draw(body);

                Path2D second = new Path2D.Double();
                second.moveTo(22, 21);
                second.lineTo(22, 19);
                second.curveTo(22, 17.2, 20.8, 15.7, 19, 15.1);
                g2.draw(second);

                Path2D head = new Path2D.Double();
                head.moveTo(16, 3.1);
                head.curveTo(19.2, 3.9, 19.2, 10.1, 16, 10.9);
                g2.draw(head);
                break;

            case USER:
                g2.draw(new Ellipse2D.Double(8, 4, 8, 8));

                Path2D shoulders = new Path2D.Double();
                shoulders.moveTo(4, 21);
                shoulders.lineTo(4, 20);
                shoulders.curveTo(4, 16.7, 6.7, 14, 10, 14);
                shoulders.lineTo(14, 14);
                shoulders.curveTo(17.3, 14, 20, 16.7, 20, 20);
                shoulders.lineTo(20, 21);
                g2.draw(shoulders);
                break;

            case PLUS:
                g2.draw(new Ellipse2D.Double(3, 3, 18, 18));
                g2.draw(new Line2D.Double(12, 8, 12, 16));
                g2.draw(new Line2D.Double(8, 12, 16, 12));
                break;

            case SEARCH:
                g2.draw(new Ellipse2D.Double(4, 4, 14, 14));
                g2.draw(new Line2D.Double(20, 20, 16.5, 16.5));
                break;
        }

        g2.dispose();
    }

    @Override
    public int getIconWidth() {
        return size;
    }

    @Override
    public int getIconHeight() {
        return size;
    }
}