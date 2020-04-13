package gui;

import java.awt.*;

/**
 * This class represents an arbitrary element that can be drown
 * */
public interface Drawable {

    /**
     * The method that draw a Drawable
     * @param g the graphics which should draw this elements
     * @param selected an arbitrary parameter used to represents selected elements
     * */
    void draw(Graphics2D g, boolean selected);

    /**
     * The method that check if a point is inside it
     * @param x the x of the point to check
     * @param y the y of the point to check
     * @return true if (x, y) is inside the Drawable element; false otherwise
     * */
    boolean contains(int x, int y);

    /**
     * The method that draw a Drawable
     * @param g the graphics which should draw this elements
     * */
    default void draw(Graphics2D g) {
        draw(g, false);
    }
}
