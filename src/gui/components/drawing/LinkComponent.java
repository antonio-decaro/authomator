package gui.components.drawing;

import gui.Drawable;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

class LinkComponent implements Drawable {

    public LinkComponent(StateComponent comp1, StateComponent comp2, String label) {
        this.label = label;
        this.comp1 = comp1;
        this.comp2 = comp2;
    }

    @Override
    public void draw(Graphics2D g, boolean selected) {

        if (selected)
            g.setColor(Color.RED);
        else
            g.setColor(Color.BLACK);

        g.setStroke(new BasicStroke(1.2f));

        if (comp1.getX() != comp2.getX() && comp1.getY() != comp2.getY())
            drawArrow(g);
        else
            drawLoop(g);
    }

    @Override
    public boolean contains(int x, int y) {
        if (line != null)
            return line.intersects(x, y, 5, 5);
        else if (arc != null)
            return arc.intersects(x, y, 5, 5);
        else
            return false;
    }

    public StateComponent getComp1() {
        return comp1;
    }

    public void setComp1(StateComponent comp1) {
        this.comp1 = comp1;
    }

    public StateComponent getComp2() {
        return comp2;
    }

    public void setComp2(StateComponent comp2) {
        this.comp2 = comp2;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    private void drawArrow(Graphics2D g) {
        int radius = comp1.WIDTH / 2;

        Point2D center1 = new Point2D.Double(comp1.getX(), comp1.getY());
        Point2D center2 = new Point2D.Double(comp2.getX(), comp2.getY());
        double from = angleBetween(center1, center2);
        double to = angleBetween(center2, center1);
        Point2D pointFrom = getPointOnCircle(center1, from, radius);
        Point2D pointTo = getPointOnCircle(center2, to, radius);
        line = new Line2D.Double(pointFrom, pointTo);

        g.drawLine((int)pointFrom.getX(), (int)pointFrom.getY(), (int)pointTo.getX(), (int)pointTo.getY());

        Point2D labelPoint = new Point2D.Double((line.getX1() + line.getX2()) / 2, (line.getY1() + line.getY2()) / 2);
        drawLabel(g, labelPoint, from);
        drawArrowHead(g, pointTo, from);
    }

    private void drawLoop(Graphics2D g) {
        arc = new Arc2D.Double(comp1.getX() - 12, comp1.getY() - 50, 25, 65, -180, -180, Arc2D.OPEN);
        g.drawArc(comp1.getX() -12, comp1.getY() - 50, 25, 65, -180, -180);
        drawArrowHead(g, new Point2D.Double(comp1.getX() + 13, comp1.getY() - 20), Math.PI);

        FontMetrics metrics = g.getFontMetrics();
        double stringWidth = metrics.stringWidth(label);
        Point2D labelPoint = new Point2D.Double(comp1.getX() - stringWidth/2, comp2.getY() - 52);
        drawLabel(g, labelPoint, 0);
    }

    private void drawLabel(Graphics2D g, Point2D point, double radius) {

        AffineTransform def = g.getTransform();
        g.translate(point.getX(), point.getY());
        if (line != null)
            g.rotate(line.getX2() > line.getX1() ? radius - Math.PI/2 : radius + Math.PI/2);
        g.drawString(label, 0, 0);
        g.setTransform(def);
    }

    private void drawArrowHead(Graphics2D g, Point2D point, double radius) {
        AffineTransform def = g.getTransform();
        g.translate(point.getX(), point.getY());
        g.rotate(radius - Math.PI);

        Polygon arrowHead = new Polygon();
        arrowHead.addPoint(0, 5);
        arrowHead.addPoint(-5, -5);
        arrowHead.addPoint(5, -5);
        g.fill(arrowHead);
        g.setTransform(def);
    }

    private double angleBetween(Point2D from, Point2D to) {
        double x = from.getX();
        double y = from.getY();

        double deltaX = to.getX() - x;
        double deltaY = to.getY() - y;

        double rotation = -Math.atan2(deltaX, deltaY);
        rotation = Math.toRadians(Math.toDegrees(rotation) + 180);

        return rotation;
    }

    private Point2D getPointOnCircle(Point2D center, double radians, double radius) {
        double x = center.getX();
        double y = center.getY();

        radians = radians - Math.toRadians(90.0);
        double xPosy = Math.round((float) (x + Math.cos(radians) * radius));
        double yPosy = Math.round((float) (y + Math.sin(radians) * radius));

        return new Point2D.Double(xPosy, yPosy);
    }

    private String label;
    private StateComponent comp1, comp2;
    private Line2D line;
    private Arc2D arc;
}
