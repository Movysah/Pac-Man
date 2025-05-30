import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.awt.Point;

public class Frame implements KeyListener {


    JFrame frame;
    private Tile[][] tiles;//[x][y]
    JPanel mapPanel = new JPanel();
    JPanel pacManPanel = new JPanel();
    JLabel pacManLabel = new JLabel();
    PacMan pacMan = new PacMan();
    private Ghost[] ghosts = new Ghost[4];
    private JPanel[] ghostPanels = new JPanel[4];
    private double[] ghostX = new double[4], ghostY = new double[4];
    private int[] ghostTargetX = new int[4], ghostTargetY = new int[4];
    private int score = 0;
    private final String[] ghostImages = {"Orange.png", "Red.png", "Pink.png", "Blue.png"};
    private long[] frightenedEndTime = new long[4];
    private boolean[] blinking = new boolean[4];
    private final Point[] ghostCorners = {
            new Point(0, 0),         // Orange: top-left
            new Point(18, 0),        // Red: top-right
            new Point(0, 20),        // Pink: bottom-left
            new Point(18, 20)        // Blue: bottom-right
    };
    private JLabel scoreLabel = new JLabel("Score: 0");
    private boolean pacmanMouthOpen = true;
    private BufferedImage pacmanClosedBase = ImageIO.read(new File("Pac-Man_Closed.png"));
    private int pacmanMouthFrame = 0;
    private final int PACMAN_MOUTH_ANIM_RATE = 3;
    JLabel controlsLabel = new JLabel("Controls: W = Up, A = Left, S = Down, D = Right");
    private int lives = 3;
    private JLabel livesLabel;


    private boolean isValid(int x, int y, Tile[][] tiles) {
        return x >= 0 && x < tiles.length &&
                y >= 0 && y < tiles[0].length &&
                tiles[x][y].getTileState() != TileState.WALL;
    }

    private boolean allDotsCleared() {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (tiles[i][j].getTileState() == TileState.DOT) {
                    return false;
                }
            }
        }
        return true;
    }

    public BufferedImage rotateImage(BufferedImage img, double angle) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage rotated = new BufferedImage(w, h, img.getType());
        Graphics2D g2d = rotated.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.rotate(Math.toRadians(angle), w / 2.0, h / 2.0);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();
        return rotated;
    }

    BufferedImage pacmanBase = ImageIO.read(new File("Pac-Man.png"));


    public Frame() throws IOException {
        MapLoad mapLoad = new MapLoad();


        pacManLabel.setIcon(new ImageIcon("Pac-Man.png"));
        //  pacManLabel.setBorder(new LineBorder(Color.red));
        pacManLabel.setBounds(0, 0, 40, 40);
        pacManLabel.setBackground(new Color(0, 0, 0));
        pacManLabel.setOpaque(false);


        pacManPanel.setLayout(null);
        pacManPanel.setBounds(0, 0, 40, 40);
        pacManPanel.add(pacManLabel);
        pacManPanel.setOpaque(false);


        frame = new JFrame();
        frame.setSize(19 * 40 + 16 + 200, 21 * 40 + 39);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.addKeyListener(this);

        mapPanel.setSize(frame.getWidth() - 200, frame.getHeight());
        mapPanel.setBounds(0, 0, 19 * 40, 21 * 40);
        mapPanel.setLayout(new GridLayout(21, 19));


        tiles = new Tile[19][21];


        for (int i = 0; i < 19 * 21; i++) {
            JPanel panel = new JPanel();


            tiles[(i % 19)][i / 19] = new Tile(panel);
            tiles[(i % 19)][(i / 19)].setTileState(mapLoad.steteOfTile(i % 19, i / 19));
            tiles[(i % 19)][i / 19].updateToState();
            mapPanel.add(panel);


        }

        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setBounds(mapPanel.getWidth(), 10, frame.getWidth() - mapPanel.getWidth(), 30);
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(scoreLabel);

        livesLabel = new JLabel("Lives: " + lives);
        livesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        livesLabel.setBounds(mapPanel.getWidth() + 20, 50, frame.getWidth() - mapPanel.getWidth(), 30);
        livesLabel.setForeground(Color.RED);
        frame.add(livesLabel);

        controlsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        controlsLabel.setText("<html>Controls:<br>W = Up<br>A = Left<br>S = Down<br>D = Right</html>");
        controlsLabel.setBounds(mapPanel.getWidth()+20, 80, frame.getWidth() - mapPanel.getWidth(), 100);
        frame.add(controlsLabel);


        JLayeredPane layeredPane = new JLayeredPane();


        //layeredPane.add(ghostPanel, JLayeredPane.PALETTE_LAYER);


        // In Frame.java, update the ghost instantiation in the constructor:
        //   String[] ghostImages = {"Orange.png", "Red.png", "Pink.png", "Blue.png"};
        int[][] ghostStarts = {{10, 9}, {9, 8}, {9, 9}, {8, 9}};

        for (int i = 0; i < 4; i++) {
            ghostPanels[i] = new JPanel();
            ghostPanels[i].setOpaque(false);// Initialize the panel
            switch (i) {
                case 0:
                    ghosts[i] = new OrangeGhost(ghostStarts[i][0], ghostStarts[i][1]);
                    break;
                case 1:
                    ghosts[i] = new RedGhost(ghostStarts[i][0], ghostStarts[i][1]);
                    break;
                case 2:
                    ghosts[i] = new PinkGhost(ghostStarts[i][0], ghostStarts[i][1]);
                    break;
                case 3:
                    ghosts[i] = new BlueGhost(ghostStarts[i][0], ghostStarts[i][1]);
                    break;
            }
            ghostPanels[i].setBounds(ghostStarts[i][0] * 40, ghostStarts[i][1] * 40, 40, 40);
            JLabel ghostLabel = new JLabel(new ImageIcon(ghostImages[i]));
            ghostPanels[i].add(ghostLabel);
            layeredPane.add(ghostPanels[i], JLayeredPane.PALETTE_LAYER);
            ghostX[i] = ghostStarts[i][0];
            ghostY[i] = ghostStarts[i][1];

        }




        layeredPane.add(mapPanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(pacManPanel, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(scoreLabel, JLayeredPane.MODAL_LAYER);
        layeredPane.add(controlsLabel, JLayeredPane.MODAL_LAYER);
        layeredPane.add(livesLabel, JLayeredPane.MODAL_LAYER);

        frame.setLayeredPane(layeredPane);


        frame.setVisible(true);
    }

    private void updateScore(int points) {
        score += points;
        scoreLabel.setText("Score: " + score);
    }


    public void setpacManPosition(double x, double y) {


        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 21; j++) {
                tiles[i][j].label.setBorder(new LineBorder(Color.black));
            }
        }
        // tiles[pacMan.getXloc()][pacMan.getYloc()].label.setBorder(new LineBorder(Color.green));

        if (x == -0.9) {
            x = 18;
        } else if (x == 18) {
            x = -0.9;
        }


        pacManPanel.setLocation((int) (40 * x), (int) (40 * y));

        pacMan.setxPosition(x);
        pacMan.setyPosition(y);

    }


    public void eat() {
        if (tiles[pacMan.getXloc()][pacMan.getYloc()].getTileState().equals(TileState.DOT)) {
            tiles[pacMan.getXloc()][pacMan.getYloc()].setTileState(TileState.Empty);
            updateScore(10); // Add 10 points for eating a dot
        } else if (tiles[pacMan.getXloc()][pacMan.getYloc()].getTileState().equals(TileState.POWER_UP)) {
            tiles[pacMan.getXloc()][pacMan.getYloc()].setTileState(TileState.Empty);
            updateScore(50); // Add 50 points for eating a power-up

            for (int i = 0; i < ghosts.length; i++) {
                ghosts[i].setFrightened(true);
                frightenedEndTime[i] = System.currentTimeMillis() + 10000; // 10 seconds
                blinking[i] = false;
            }
        }
    }

    public TileState getStateOnPosition(double xpos, double ypos) {

        return tiles[(int) xpos][(int) ypos].getTileState();
    }


    public void movePacMan() {

        pacMan.updateLocation();

        eat();
        eat();
        if (allDotsCleared()) {
            JOptionPane.showMessageDialog(frame, "Level Complete!"+ "\nScore: " + score);
            // Optionally, reset the level or load a new one here
            System.exit(0); // Or implement level reset logic
        }

        boolean canChangeDirection = false;


        switch (pacMan.getReqDirection()) {
            case 0:
                if (!getStateOnPosition(pacMan.getxPosition(), pacMan.getyPosition() - 0.1).equals(TileState.WALL)
                        && (!getStateOnPosition(pacMan.getxPosition() + 0.9, pacMan.getyPosition() - 0.1).equals(TileState.WALL))) {
                    setpacManPosition(pacMan.getxPosition(), pacMan.getyPosition() - 0.1);
                    pacMan.updateDirection();
                    canChangeDirection = true;
                }
                break;
            case 1:
                if (!getStateOnPosition(pacMan.getxPosition() + 1.0, pacMan.getyPosition()).equals(TileState.WALL)
                        && (!getStateOnPosition(pacMan.getxPosition() + 1.0, pacMan.getyPosition() + 0.9).equals(TileState.WALL))) {
                    setpacManPosition(pacMan.getxPosition() + 0.1, pacMan.getyPosition());
                    pacMan.updateDirection();
                    canChangeDirection = true;
                }
                break;
            case 2:
                if (!getStateOnPosition(pacMan.getxPosition(), pacMan.getyPosition() + 1.0).equals(TileState.WALL)
                        && (!getStateOnPosition(pacMan.getxPosition() + 0.9, pacMan.getyPosition() + 1.0).equals(TileState.WALL))) {
                    setpacManPosition(pacMan.getxPosition(), pacMan.getyPosition() + 0.1);
                    pacMan.updateDirection();
                    canChangeDirection = true;
                }
                break;
            case 3:
                if (!getStateOnPosition(pacMan.getxPosition() - 0.1, pacMan.getyPosition()).equals(TileState.WALL)
                        && (!getStateOnPosition(pacMan.getxPosition() - 0.1, pacMan.getyPosition() + 0.99).equals(TileState.WALL))) {
                    setpacManPosition(pacMan.getxPosition() - 0.1, pacMan.getyPosition());
                    pacMan.updateDirection();
                    canChangeDirection = true;
                }
                break;
        }
        pacmanMouthFrame++;
        if (pacmanMouthFrame >= PACMAN_MOUTH_ANIM_RATE) {
            double angle = 0;
            switch (pacMan.getDirection()) {
                case 0: angle = 270; break;
                case 1: angle = 0; break;
                case 2: angle = 90; break;
                case 3: angle = 180; break;
            }
            BufferedImage base = pacmanMouthOpen ? pacmanBase : pacmanClosedBase;
            BufferedImage rotated = rotateImage(base, angle);
            pacManLabel.setIcon(new ImageIcon(rotated));
            pacmanMouthOpen = !pacmanMouthOpen;
            pacmanMouthFrame = 0;
        }

        if (canChangeDirection) {
            return;
        }

        switch (pacMan.getDirection()) {
            case 0:
                if (!getStateOnPosition(pacMan.getxPosition(), pacMan.getyPosition() - 0.1).equals(TileState.WALL)
                        && (!getStateOnPosition(pacMan.getxPosition() + 0.9, pacMan.getyPosition() - 0.1).equals(TileState.WALL))) {
                    setpacManPosition(pacMan.getxPosition(), pacMan.getyPosition() - 0.1);
                    return;
                }
                break;
            case 1:
                if (!getStateOnPosition(pacMan.getxPosition() + 1.0, pacMan.getyPosition()).equals(TileState.WALL)
                        && (!getStateOnPosition(pacMan.getxPosition() + 1.0, pacMan.getyPosition() + 0.9).equals(TileState.WALL))) {
                    setpacManPosition(pacMan.getxPosition() + 0.1, pacMan.getyPosition());
                    return;
                }
                break;
            case 2:
                if (!getStateOnPosition(pacMan.getxPosition(), pacMan.getyPosition() + 1.0).equals(TileState.WALL)
                        && (!getStateOnPosition(pacMan.getxPosition() + 0.9, pacMan.getyPosition() + 1.0).equals(TileState.WALL))) {
                    setpacManPosition(pacMan.getxPosition(), pacMan.getyPosition() + 0.1);
                    return;
                }
                break;
            case 3:
                if (!getStateOnPosition(pacMan.getxPosition() - 0.1, pacMan.getyPosition()).equals(TileState.WALL)
                        && (!getStateOnPosition(pacMan.getxPosition() - 0.1, pacMan.getyPosition() + 0.9).equals(TileState.WALL
                ))) {
                    setpacManPosition(pacMan.getxPosition() - 0.1, pacMan.getyPosition());
                    return;
                }
                break;
        }


    }


    private int[][] directions = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};


    private int[] ghostDirX = new int[4];
    private int[] ghostDirY = new int[4];


    public void moveGhosts() {
        double ghostSpeed = 0.083333333333333333; // 1/11th of a tile per frame

        for (int i = 0; i < ghosts.length; i++) {
            int ghostXInt = (int) Math.round(ghostX[i]);
            int ghostYInt = (int) Math.round(ghostY[i]);
            Point pacmanPos = new Point(pacMan.getXloc(), pacMan.getYloc());
            int pacmanDir = pacMan.getDirection();
            Point blinkyPos = new Point(ghosts[1].getXloc(), ghosts[1].getYloc());

            if (Math.abs(ghostX[i] - ghostXInt) < 0.02 && Math.abs(ghostY[i] - ghostYInt) < 0.02) {
                ghostX[i] = ghostXInt;
                ghostY[i] = ghostYInt;
            }

            boolean atCenter = Math.abs(ghostX[i] - ghostXInt) < 0.01 && Math.abs(ghostY[i] - ghostYInt) < 0.01;

            // 1. Update direction if at center
            if (atCenter) {
                Point nextStep;
                if (ghosts[i].isFrightened()) {
                    // Move towards assigned corner
                    Point target = ghostCorners[i];
                    int bestDx = 0, bestDy = 0;
                    int minDist = Integer.MAX_VALUE;
                    for (int[] dir : directions) {
                        int nx = ghostXInt + dir[0];
                        int ny = ghostYInt + dir[1];
                        if (isValid(nx, ny, tiles)) {
                            int dist = Math.abs(nx - target.x) + Math.abs(ny - target.y);
                            if (dist < minDist) {
                                minDist = dist;
                                bestDx = dir[0];
                                bestDy = dir[1];
                            }
                        }
                    }
                    nextStep = new Point(ghostXInt + bestDx, ghostYInt + bestDy);
                } else {
                    nextStep = ghosts[i].getNextMove(tiles, pacmanPos, pacmanDir, blinkyPos);
                }
                int dx = nextStep.x - ghostXInt;
                int dy = nextStep.y - ghostYInt;
                if (Math.abs(dx) > 1 || Math.abs(dy) > 1) {
                    // If the next step is more than one tile away, clamp it to one tile
                    dx = 0;
                    dy = 0;
                }

                ghostDirX[i] = dx;
                ghostDirY[i] = dy;
            }

            // 2. Try to move in the current direction
            double nextX = ghostX[i] + ghostDirX[i] * ghostSpeed;
            double nextY = ghostY[i] + ghostDirY[i] * ghostSpeed;
            int nextTileX = (int) Math.floor(nextX + 0.00 * ghostDirX[i]);
            int nextTileY = (int) Math.floor(nextY + 0.00 * ghostDirY[i]);

            if (isValid(nextTileX, nextTileY, tiles)) {
                ghostX[i] = nextX;
                ghostY[i] = nextY;
            }

            long timeLeft = frightenedEndTime[i] - System.currentTimeMillis();
            if (timeLeft <= 2000 && timeLeft > 0) {
                blinking[i] = true;
            } else {
                blinking[i] = false;
            }
            if (timeLeft <= 0 && ghosts[i].isFrightened()) {
                ghosts[i].setFrightened(false);
                blinking[i] = false;
            }


            JLabel ghostLabel = (JLabel) ghostPanels[i].getComponent(0);
            if (ghosts[i].isFrightened()) {
                if (blinking[i]) {
                    System.out.println("Blinking ghost " + i + " at (" + ghostX[i] + ", " + ghostY[i] + ")");
                    System.out.println(System.currentTimeMillis());
                    // Alternate every 200ms
                    if ((System.currentTimeMillis() / 200) % 2 == 0) {
                        ghostLabel.setIcon(new ImageIcon("frightened.png"));
                    } else {
                        ghostLabel.setIcon(new ImageIcon(ghostImages[i])); // or ghostImages[i] for original color
                    }
                } else {
                    ghostLabel.setIcon(new ImageIcon("frightened.png"));
                }
            } else {
                ghostLabel.setIcon(new ImageIcon(ghostImages[i]));
            }


            double dist = Math.hypot(ghostX[i] - pacMan.getxPosition(), ghostY[i] - pacMan.getyPosition());
            if (ghosts[i].isFrightened()) {
                if (dist < 0.9) {
                    // Pac-Man eats frightened ghost
                    score += 100;
                    frame.setTitle("Pac-Man - Score: " + score);
                    int[][] ghostStarts = {{10, 9}, {9, 8}, {9, 9}, {8, 9}};
                    ghostX[i] = ghostStarts[i][0];
                    ghostY[i] = ghostStarts[i][1];
                    ghostDirX[i] = 0;
                    ghostDirY[i] = 0;


                    ghosts[i].setFrightened(false);
                    ghostPanels[i].setLocation((int) (ghostX[i] * 40), (int) (ghostY[i] * 40));
                    continue;
                }
            } else {
                if (dist < 0.9) {
                    lives--;
                    livesLabel.setText("Lives: " + lives);
                    if (lives <= 0) {
                        JOptionPane.showMessageDialog(frame, "Game Over! Pac-Man was caught.");
                        System.exit(0);
                    } else {
                        // Reset Pac-Man and ghosts to starting positions
                        pacMan.setxPosition(9);
                        pacMan.setyPosition(15);
                        for (int j = 0; j < ghosts.length; j++) {
                            int[][] ghostStarts = {{10, 9}, {9, 8}, {9, 9}, {8, 9}};
                            ghostX[j] = ghostStarts[j][0];
                            ghostY[j] = ghostStarts[j][1];
                            ghostDirX[j] = 0;
                            ghostDirY[j] = 0;
                            ghosts[j].setFrightened(false);
                            ghostPanels[j].setLocation((int) (ghostX[j] * 40), (int) (ghostY[j] * 40));
                        }
                        // Optionally, pause briefly before resuming
                    }
                    continue;
                }
            }

            // Snap to grid to avoid floating-point drift
            ghostX[i] = Math.round(ghostX[i] * 10000.0) / 10000.0;
            ghostY[i] = Math.round(ghostY[i] * 10000.0) / 10000.0;

            System.out.println("Ghost " + i + " Position: (" + ghostX[i] + ", " + ghostY[i] + ") Direction: (" + ghostDirX[i] + ", " + ghostDirY[i] + ")");

            ghosts[i].setPosition((int) Math.round(ghostX[i]), (int) Math.round(ghostY[i]));
            ghostPanels[i].setLocation((int) (ghostX[i] * 40), (int) (ghostY[i] * 40));

            if (ghosts[i].isFrightened() && dist < 0.9) {
                updateScore(100); // Add 100 points for eating a frightened ghost
                // Reset ghost position and state
                int[][] ghostStarts = {{10, 9}, {9, 8}, {9, 9}, {8, 9}};
                ghostX[i] = ghostStarts[i][0];
                ghostY[i] = ghostStarts[i][1];
                ghostDirX[i] = 0;
                ghostDirY[i] = 0;
                ghosts[i].setFrightened(false);
                ghostPanels[i].setLocation((int) (ghostX[i] * 40), (int) (ghostY[i] * 40));
                continue;
            }
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {


        switch (e.getKeyCode()) {
            case 87:
                pacMan.setReqDirection((short) 0);
                break;
            case 68:
                pacMan.setReqDirection((short) 1);
                break;
            case 83:
                pacMan.setReqDirection((short) 2);
                break;
            case 65:
                pacMan.setReqDirection((short) 3);
                break;


        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
