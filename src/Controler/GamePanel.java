package Controler;


import Model.Obstacle;
import Model.Player;
//import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private java.util.List<Obstacle> obstacles = new ArrayList<>();
    private int obstacleTimer = 0;
    private Player player;
    private double hexagonAngle = 0;
    private static final int HEX_RADIUS = 90;
    private static final double STEP = 0.04;
    private int currentEdge = 0;
    private double edgeProgress = 0.0;
    private Point[] hexVertices = new Point[6];


    public GamePanel(Player player) {
        this.player = player;
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        timer = new Timer(16, this); 
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


// Get edge points
        Point a = hexVertices[currentEdge];
        Point b = hexVertices[(currentEdge + 1) % 6];

// Edge vector
        double dx = b.x - a.x;
        double dy = b.y - a.y;
        double edgeLength = Math.sqrt(dx * dx + dy * dy);

// Unit edge direction
        double ux = dx / edgeLength;
        double uy = dy / edgeLength;

// Normal vector
        double nx = -uy;
        double ny = ux;

// Position along edge
        double edgeCenterX = a.x + dx * edgeProgress;
        double edgeCenterY = a.y + dy * edgeProgress;

// Offset center outward by gap
        double gap = -10;
        double cx = edgeCenterX + gap * nx;
        double cy = edgeCenterY + gap * ny;

// Triangle size
        double baseHalf = 8;
        double tipLength = -14;

// Base points
        double baseX1 = cx + baseHalf * ux;
        double baseY1 = cy + baseHalf * uy;
        double baseX2 = cx - baseHalf * ux;
        double baseY2 = cy - baseHalf * uy;

// Tip point
        double tipX = cx + tipLength * nx;
        double tipY = cy + tipLength * ny;

// Draw cursor
        g2d.setColor(Color.CYAN);
        g2d.fillPolygon(
                new int[]{(int) tipX, (int) baseX1, (int) baseX2},
                new int[]{(int) tipY, (int) baseY1, (int) baseY2},
                3
        );


        for (Obstacle ob : obstacles) {
            ob.draw(g2d, hexagonAngle);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        hexagonAngle += 0.01;
        repaint();


        hexagonAngle += 0.01;
        obstacleTimer++;
        if (obstacleTimer % 60 == 0) {
            Set<Integer> chosenEdges = new HashSet<>();
            int gapEdge = new Random().nextInt(6); // اون یه دیواری که حداقل باید باشه
            for (int i = 0; i < 6; i++) {
                if (i != gapEdge) {
                    chosenEdges.add(i);
                }
            }
            // حداقل یه دیوار
            int toRemove = new Random().nextInt(5); 
            List<Integer> list = new ArrayList<>(chosenEdges);
            Collections.shuffle(list);
            for (int i = 0; i < toRemove; i++) {
                chosenEdges.remove(list.get(i));
            }
            for (int edge : chosenEdges) {
                obstacles.add(new Obstacle(edge));
            }
        }
        for (Obstacle ob : obstacles) {
            ob.update();
        }

        //check collision
        double dx = hexVertices[currentEdge].x - hexVertices[(currentEdge + 1) % 6].x;
        double dy = hexVertices[currentEdge].y - hexVertices[(currentEdge + 1) % 6].y;
        double edgeLen = Math.sqrt(dx * dx + dy * dy);
        double cursorDistance = Math.sqrt(Math.pow(edgeLen * edgeProgress, 2) + Math.pow(HEX_RADIUS + 10, 2));

        for (Obstacle ob : obstacles) {
            if (ob.isColliding(currentEdge, cursorDistance, 6)) {
                System.out.println("Collision detected. Bitches");
                timer.stop();
                return;
            }
        }

        obstacles.removeIf(Obstacle::isOffScreen);
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
