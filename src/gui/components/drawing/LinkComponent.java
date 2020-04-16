package gui.components.drawing;

import gui.Drawable;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

/**
 * This class represents the GUI representation of an edge that binds two states
 * */
class LinkComponent implements Drawable {

    /**
     * Class constructor
     * @param comp1 the first state to bind
     * @param comp2 the second state to bind
     * @param label the label over the edge
     * */
    public LinkComponent(StateComponent comp1, StateComponent comp2, String label) {
        this.label = label;
        this.comp1 = comp1;
        this.comp2 = comp2;

        pointFrom = new Point2D.Double(comp1.getX(), comp1.getY());
        pointTo = new Point2D.Double(comp2.getX(), comp2.getY());
        curvePoint = new Point2D.Double((pointFrom.getX() + pointTo.getX()) / 2, (pointFrom.getY() + pointTo.getY()) / 2);
    }

    @Override
    public void draw(Graphics2D g, boolean selected) {

        if (selected)
            g.setColor(Color.RED);
        else
            g.setColor(Color.BLACK);

        g.setStroke(new BasicStroke(1.2f));

        if (comp1.getX() == comp2.getX() && comp1.getY() == comp2.getY())
            drawLoop(g);
        else
            drawArrow(g);
    }

    @Override
    public boolean contains(int x, int y) {
        return path.intersects(x, y, 5, 5);
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

    /**
     * This private method draw an arrow from the comp1 to the comp2
     * */
    private void drawArrow(Graphics2D g) {
        int radius = comp1.WIDTH / 2;

        Point2D center1 = new Point2D.Double(comp1.getX(), comp1.getY());
        Point2D center2 = new Point2D.Double(comp2.getX(), comp2.getY());
        double from = angleBetween(center1, center2);
        double to = angleBetween(center2, center1);
        pointFrom = getPointOnCircle(center1, from, radius);
        pointTo = getPointOnCircle(center2, to, radius);
        if (!customCurvePoint)
            curvePoint = new Point2D.Double((pointFrom.getX() + pointTo.getX()) / 2, (pointFrom.getY() + pointTo.getY()) / 2);

        path = new Path2D.Double();
        path.moveTo(pointFrom.getX(), pointFrom.getY());
        path.curveTo(pointFrom.getX(), pointFrom.getY(), curvePoint.getX(), curvePoint.getY(), pointTo.getX(), pointTo.getY());

        g.draw(path);

        Point2D labelPoint = new Point2D.Double(curvePoint.getX(), curvePoint.getY());
        drawLabel(g, labelPoint, from);
        drawArrowHead(g, pointTo, from);
    }

    /**
     * This private method draws a loop edge on the same component (comp1 = comp2)
     * */
    private void drawLoop(Graphics2D g) {
        Arc2D arc = new Arc2D.Double(comp1.getX() - 12, comp1.getY() - 50, 25, 65, -180, -180, Arc2D.OPEN);
        path = new Path2D.Double(arc);
        g.draw(path);
        drawArrowHead(g, new Point2D.Double(comp1.getX() + 13, comp1.getY() - 20), Math.PI);

        FontMetrics metrics = g.getFontMetrics();
        double stringWidth = metrics.stringWidth(label);
        Point2D labelPoint = new Point2D.Double(comp1.getX() - stringWidth/2, comp1.getY() - 52);
        drawLabel(g, labelPoint, 0);
    }

    /**
     * This private method draws the label on the mid of the edge
     * */
    private void drawLabel(Graphics2D g, Point2D point, double radius) {

        AffineTransform def = g.getTransform();
        g.translate(point.getX(), point.getY());
        if (!pointFrom.equals(pointTo))
            g.rotate(pointTo.getX() > pointFrom.getX() ? radius - Math.PI/2 : radius + Math.PI/2);
        g.drawString(label, 0, 0);
        g.setTransform(def);
    }

    /**
     * This private method draws an arrowhead on point
     * @param point point on which draw the arrowhead
     * @param radius the radius of the arrowhead (for rotation)
     * */
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

    /**
     * This private method calculate the angle between to points
     * */
    private double angleBetween(Point2D from, Point2D to) {
        double x = from.getX();
        double y = from.getY();

        double deltaX = to.getX() - x;
        double deltaY = to.getY() - y;

        double rotation = -Math.atan2(deltaX, deltaY);
        rotation = Math.toRadians(Math.toDegrees(rotation) + 180);

        return rotation;
    }

    public Point2D getCurvePoint() {
        return curvePoint;
    }

    public void setCurvePoint(Point2D curvePoint) {
        this.curvePoint = curvePoint;
    }

    public Point2D getPointFrom() {
        return pointFrom;
    }

    public void setPointFrom(Point2D pointFrom) {
        this.pointFrom = pointFrom;
    }

    public Point2D getPointTo() {
        return pointTo;
    }

    public void setPointTo(Point2D pointTo) {
        this.pointTo = pointTo;
    }

    public boolean isCustomCurvePoint() {
        return customCurvePoint;
    }

    public void setCustomCurvePoint(boolean customCurvePoint) {
        this.customCurvePoint = customCurvePoint;
    }

    /**
     * This private method, given a center point of the circle, calculate the point on circumference.
     * */
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
    private boolean customCurvePoint;
    private Point2D pointFrom, pointTo, curvePoint;
    private Path2D path;
}
