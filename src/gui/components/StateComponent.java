package gui.components;

import gui.Drawable;

import java.awt.*;

public class StateComponent implements Drawable {

    public StateComponent(int x, int y, ContentPanel container) {
        this.x = x;
        this.y = y;
        this.label = "";
        this.container = container;
    }

    @Override
    public void draw(Graphics2D g, boolean selected) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillOval(x - WIDTH/2, y - HEIGHT/2, WIDTH, HEIGHT);
        if (selected) {
            g.setColor(Color.BLUE);
            g.setStroke(new BasicStroke(2.5f));
        } else {
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(1.5f));
        }
        g.drawOval(x - WIDTH/2, y - HEIGHT/2, WIDTH, HEIGHT);
        if (isFinal) {
            g.setStroke(new BasicStroke(1));
            final int OFFS = 5;
            g.drawOval(x - WIDTH/2 + OFFS, y - HEIGHT/2 + OFFS, WIDTH - OFFS*2, HEIGHT - OFFS*2);
        }
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        if (isInitial) {
            int stringX = x + (WIDTH - metrics.stringWidth(label))/2 + 2;
            int stringY = y + ((HEIGHT - metrics.getHeight())/2) + metrics.getAscent() + 2;
            g.drawString("Init", stringX, stringY);
        }
        int stringX = (x - WIDTH/2) + (WIDTH - metrics.stringWidth(label))/2;
        int stringY = (y - HEIGHT/2) + ((HEIGHT - metrics.getHeight())/2) + metrics.getAscent();
        g.drawString(label, stringX, stringY);
    }

    @Override
    public boolean contains(int x, int y) {
        Rectangle rectangle = new Rectangle(this.x - WIDTH / 2, this.y - HEIGHT / 2, WIDTH, HEIGHT);
        return rectangle.contains(x, y);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        if (x - WIDTH / 2 > 0 && x + WIDTH / 2 < (int) container.getSize().getWidth())
            this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        if (y - HEIGHT / 2 > 0 && y + HEIGHT / 2 < (int) container.getSize().getHeight())
            this.y = y;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public boolean isInitial() {
        return isInitial;
    }

    public void setInitial(boolean initial) {
        isInitial = initial;
    }

    public void setFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }

    public final int WIDTH = 40, HEIGHT = 40;
    private ContentPanel container;
    private int x, y;
    private String label;
    private boolean isInitial;
    private boolean isFinal;
}
