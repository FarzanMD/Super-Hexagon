package Controler;


import Model.*;
//import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

import static Model.HistoryRecord.loadHistory;
import static Model.HistoryRecord.saveHistory;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private java.util.List<Obstacle> obstacles = new ArrayList<>();
    private int obstacleTimer = 0;
    private Player player;
    private double hexagonAngle = 0;
    private static final int HEX_RADIUS = 90;
    private static final double STEP = 0.08;
    private int currentEdge = 0;
    private double edgeProgress = 0.0;
    private Point[] hexVertices = new Point[6];
    private int score = 0;
    double obstacleSpeed = 1.5;
    double rotatingSpeed = 0.01;

    //colors
    private float hueEven = 0.75f;
    private float hueOdd = 0.86f;
    private float hueStepEven = 0.0018f;
    private float hueStepOdd = 0.0023f;
    private boolean hueEvenIncreasing = true;
    private boolean hueOddIncreasing = false;

    //pause mechanics
    private boolean isPaused = false;

    //Patern mechs
    private boolean lastInBetweenWasEven = false; // False means odd, true means even was last





    public GamePanel(Player player) {
        this.player = player;
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        timer = new Timer(16, this); 
    }

    public void start() {
        timer.start();
        if (GameSettings.isMusicEnabled()) {
            MusicPlayer.GET_INSTANCE().play();
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        //drawing score
        AffineTransform originalTransform = g2d.getTransform();
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("Score: " + score, 20, 30);

        //drawing best score
        List<HistoryRecord> sorted = loadHistory();
        sorted.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
        int bestScore = sorted.getFirst().getScore();
        g2d.drawString("Best Score: " + bestScore , 20 , 60);

        g2d.setTransform(originalTransform);
        //g2d.translate(getWidth() / 2, getHeight() / 2);

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

        //rotating sectors(???)
        //List<Polygon> sectors = new ArrayList<>();
        //Point center = new Point(0, 0); // after translate, this is center

        // Land division colors
        Color[] sectorColors = new Color[6];
        for (int i = 0; i < 6; i++) {
            float localHue;
            Color base;
            if (i % 2 == 0) {
                localHue = (hueEven + (i * 0.005f)) % 1f;
                base = Color.getHSBColor(localHue, 0.3f, 1f);
            } else {
                localHue = (hueOdd + (i * 0.005f)) % 1f;
                base = Color.getHSBColor( localHue,0.3f, 1f);
            }

            //Color base = Color.getHSBColor(localHue, 1f, 1f);
            sectorColors[i] = new Color(base.getRed(), base.getGreen(), base.getBlue(), 100);
        }




        Point center = new Point(0, 0);
        int extension = 1000; // how far to extend beyond the hex

        for (int i = 0; i < 6; i++) {
            // angles for the two bounding edges
            double angle1 = i * Math.PI / 3 + hexagonAngle;
            double angle2 = (i + 1) * Math.PI / 3 + hexagonAngle;

            int x1 = (int)(extension * Math.cos(angle1));
            int y1 = (int)(extension * Math.sin(angle1));
            int x2 = (int)(extension * Math.cos(angle2));
            int y2 = (int)(extension * Math.sin(angle2));

            Polygon sector = new Polygon();
            sector.addPoint(center.x, center.y);
            sector.addPoint(x1, y1);
            sector.addPoint(x2, y2);

            g2d.setColor(sectorColors[i]);
            g2d.fillPolygon(sector);
        }






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


        //drawing Pause ( FAILED )
        /*if (isPaused) {
//            g2d.setTransform(new AffineTransform()); // reset transform
//            g2d.setColor(Color.WHITE);
//            g2d.setFont(new Font("Arial", Font.BOLD, 40));
//            g2d.drawString("PAUSED", getWidth() / 2 -10 , getHeight() / 2 +90);
            //new PauseWindow(g2d,this);
        }
         */

        //coloring hexagon after everything ( FAILED )
        /*
        Polygon coloredHex = new Polygon();
        for (int i = 0; i < 6; i++) {
            double angle = i * Math.PI / 3 + hexagonAngle;
            int x = (int) (HEX_RADIUS * Math.cos(angle));
            int y = (int) (HEX_RADIUS * Math.sin(angle));
            hex.addPoint(x, y);
            hexVertices[i] = new Point(x, y);
        }
        //g2d.drawPolygon(coloredHex);
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillPolygon(coloredHex);
*/




        for (Obstacle ob : obstacles) {
            ob.draw(g2d, hexagonAngle);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //coloring
        // Animate even-index hues
        if (hueEvenIncreasing) {
            hueEven += hueStepEven;
            if (hueEven > 0.1f) {
                hueEven = 0.1f;
                hueEvenIncreasing = false;
            }
        } else {
            hueEven -= hueStepEven;
            if (hueEven < 0.0f) {
                hueEven = 0.0f;
                hueEvenIncreasing = true;
            }
        }

// Animate odd-index hues
        if (hueOddIncreasing) {
            hueOdd += hueStepOdd;
            if (hueOdd > 0.12f) {
                hueOdd = 0.12f;
                hueOddIncreasing = false;
            }
        } else {
            hueOdd -= hueStepOdd;
            if (hueOdd < 0.02f) {
                hueOdd = 0.02f;
                hueOddIncreasing = true;
            }
        }



        //++ is too fast
        // score++;
        if (obstacleTimer % 10 == 0) {
            score++;
        }

        if (score % 50 ==0){
            rotatingSpeed+=0.0002;
        }
        hexagonAngle += rotatingSpeed;
        repaint();

        //hexagonAngle += 0.01;


        //enemy pattern
        obstacleTimer++;
        if (obstacleTimer % 100 == 0) {
            Set<Integer> chosenEdges = new HashSet<>();
            Random random = new Random();
            int patternChoice = random.nextInt(3); // Randomly choose pattern

            switch (patternChoice) {
                case 0: // Single Gap Pattern
                    int gapEdge = random.nextInt(6); // Randomly choose the gap
                    for (int i = 0; i < 6; i++) {
                        if (i != gapEdge) {
                            chosenEdges.add(i);
                        }
                    }
                    break;

                case 1: // In-Between Gap Pattern (Even or Odd edges)
                    int startEdge;
                    if (lastInBetweenWasEven) {
                        startEdge = 1;
                    } else {
                        startEdge = 0;
                    }
                    for (int i = startEdge; i < 6; i += 2) {
                        chosenEdges.add(i);
                    }
                    lastInBetweenWasEven = !lastInBetweenWasEven;
                    break;

                case 2: // Random Gaps Pattern
                    int gapEdgeRandom = random.nextInt(6); //least one gap
                    for (int i = 0; i < 6; i++) {
                        if (i != gapEdgeRandom) {
                            chosenEdges.add(i);
                        }
                    }
                    int toRemove = random.nextInt(5); // Remove 0 to 4
                    List<Integer> list = new ArrayList<>(chosenEdges);
                    Collections.shuffle(list);
                    for (int i = 0; i < toRemove; i++) {
                        chosenEdges.remove(list.get(i));
                    }
                    break;
            }

            // Spawn obstacles on chosen edges
            for (int edge : chosenEdges) {
                obstacles.add(new Obstacle(edge));
            }
        }


        if (score%50 == 0){
            obstacleSpeed+=0.01;
        }
        for (Obstacle ob : obstacles) {
            ob.update(obstacleSpeed);
        }

        //check collision
        double dx = hexVertices[currentEdge].x - hexVertices[(currentEdge + 1) % 6].x;
        double dy = hexVertices[currentEdge].y - hexVertices[(currentEdge + 1) % 6].y;
        double edgeLen = Math.sqrt(dx * dx + dy * dy);
        double cursorDistance = Math.sqrt(Math.pow(edgeLen * edgeProgress, 2) + Math.pow(HEX_RADIUS + 10, 2));


        //Game over on collision
        for (Obstacle ob : obstacles) {
            if (ob.isColliding(currentEdge, cursorDistance, 6)) {
                System.out.println("Collision detected. Bitches");
                System.out.println("Final Score: " + score);

                if (GameSettings.isHistoryEnabled()) {
                    List<HistoryRecord> records = loadHistory();
                    records.add(new HistoryRecord(score, player.getName(), LocalDateTime.now().toString()));
                    saveHistory(records);
                }
                MusicPlayer.GET_INSTANCE().stop();
                timer.stop();
                System.exit(0);
                return;
            }
        }

        obstacles.removeIf(Obstacle::isOffScreen);
        repaint();

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_ESCAPE) {
            isPaused = !isPaused;

            if (isPaused) {
                timer.stop();
                MusicPlayer.GET_INSTANCE().stop();

            } else {
                timer.start();
                MusicPlayer.GET_INSTANCE().play();
            }

            repaint();
            return;
        }

        if (isPaused) return; // ignore movement if paused

        if (code == KeyEvent.VK_RIGHT) {
            edgeProgress += STEP;
            if (edgeProgress > 1.0) {
                edgeProgress = 0.0;
                currentEdge = (currentEdge + 1) % 6;
            }
        } else if (code == KeyEvent.VK_LEFT) {
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
