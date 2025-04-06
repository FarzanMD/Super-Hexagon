package Controler;


import Model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private Player player;
    private double hexagonAngle = 0;
    private static final int HEX_RADIUS = 100;
    private static final double STEP = 0.02;
    private int currentEdge = 0;
    private double edgeProgress = 0.0;
    private Point[] hexVertices = new Point[6];

    public GamePanel(Player player) {
        this.player = player;
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        timer = new Timer(16, this); // ~60 FPS
    }

    public void start() {
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int w = getWidth();
        int h = getHeight();
        g2d.translate(w / 2, h / 2);


        g2d.setColor(Color.WHITE);
        Polygon hex = new Polygon();
        for (int i = 0; i < 6; i++) {
            double angle = i * Math.PI / 3 + hexagonAngle;
            int x = (int) (HEX_RADIUS * Math.cos(angle));
            int y = (int) (HEX_RADIUS * Math.sin(angle));
            hex.addPoint(x, y);
            hexVertices[i] = new Point(x, y);
        }
        g2d.drawPolygon(hex);


        Point a = hexVertices[currentEdge];
        Point b = hexVertices[(currentEdge + 1) % 6];
        double cx = a.x + (b.x - a.x) * edgeProgress;
        double cy = a.y + (b.y - a.y) * edgeProgress;
        double edgeAngle = Math.atan2(b.y - a.y, b.x - a.x);


        double spread = Math.PI / 15;
        int leftX = (int) (cx + 10 * Math.cos(edgeAngle - spread));
        int leftY = (int) (cy + 10 * Math.sin(edgeAngle - spread));
        int rightX = (int) (cx + 10 * Math.cos(edgeAngle + spread));
        int rightY = (int) (cy + 10 * Math.sin(edgeAngle + spread));

        g2d.setColor(Color.CYAN);
        g2d.fillPolygon(new int[]{leftX, (int) cx, rightX}, new int[]{leftY, (int) cy, rightY}, 3);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        hexagonAngle += 0.01;
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            edgeProgress += STEP;
            if (edgeProgress > 1.0) {
                edgeProgress = 0.0;
                currentEdge = (currentEdge + 1) % 6;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            edgeProgress -= STEP;
            if (edgeProgress < 0.0) {
                currentEdge = (currentEdge + 5) % 6;
                edgeProgress = 1.0;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
