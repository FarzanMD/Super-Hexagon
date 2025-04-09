package Model;

import java.awt.*;

public class Obstacle {
    private int edgeIndex;
    private double distance;
    //private static final double SPEED = 1.5;
    private static final int THICKNESS = 20;
    private static final int HEX_RADIUS = 100;

    public Obstacle(int edgeIndex) {
        this.edgeIndex = edgeIndex;
        this.distance = 600;
    }

    public void update(double Speed) {
        distance -= Speed;
    }


    public boolean isOffScreen() {
        return distance < HEX_RADIUS ;
                //- THICKNESS;
    }

    public boolean isColliding(int cursorEdge, double cursorDistance, double tolerance) {
        return this.edgeIndex == cursorEdge && Math.abs(this.distance - cursorDistance) < tolerance;
    }


    public void draw(Graphics2D g2d, double hexagonAngle) {
        double angle = edgeIndex * Math.PI / 3 + hexagonAngle;
        double nextAngle = angle + Math.PI / 3;

        int innerR = (int) distance;
        int outerR = (int) (distance + THICKNESS);

        int[] xPoints = {
                (int)(innerR * Math.cos(angle)),
                (int)(innerR * Math.cos(nextAngle)),
                (int)(outerR * Math.cos(nextAngle)),
                (int)(outerR * Math.cos(angle))
        };

        int[] yPoints = {
                (int)(innerR * Math.sin(angle)),
                (int)(innerR * Math.sin(nextAngle)),
                (int)(outerR * Math.sin(nextAngle)),
                (int)(outerR * Math.sin(angle))
        };

        g2d.setColor(new Color(0x00004E));
        g2d.fillPolygon(xPoints, yPoints, 4);
    }

    public int getEdgeIndex() {
        return edgeIndex;
    }

    public double getDistance() {
        return distance;
    }
}
