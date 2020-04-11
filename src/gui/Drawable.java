package gui;

import java.awt.*;

public interface Drawable {

    void draw(Graphics2D g, boolean selected);
    boolean contains(int x, int y);

    default void draw(Graphics2D g) {
        draw(g, false);
    }
}
