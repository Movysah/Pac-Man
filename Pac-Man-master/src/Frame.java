import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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


    private boolean isValid(int x, int y, Tile[][] tiles) {
        return x >= 0 && x < tiles.length &&
                y >= 0 && y < tiles[0].length &&
                tiles[x][y].getTileState() != TileState.WALL;
    }


    public Frame() {
        MapLoad mapLoad = new MapLoad();


        pacManLabel.setIcon(new ImageIcon("Pac-Man.png"));
        pacManLabel.setBorder(new LineBorder(Color.red));
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

        frame.setLayeredPane(layeredPane);


        frame.setVisible(true);
    }


    public void setpacManPosition(double x, double y) {


        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 21; j++) {
                tiles[i][j].label.setBorder(new LineBorder(Color.black));
            }
        }
        tiles[pacMan.getXloc()][pacMan.getYloc()].label.setBorder(new LineBorder(Color.green));

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
            score += 1;
            frame.setTitle("Pac-Man - Score: " + score);
        } else if (tiles[pacMan.getXloc()][pacMan.getYloc()].getTileState().equals(TileState.POWER_UP)) {
            tiles[pacMan.getXloc()][pacMan.getYloc()].setTileState(TileState.Empty);
            score += 20;
            frame.setTitle("Pac-Man - Score: " + score);

            for (Ghost ghost : ghosts) {
                ghost.setFrightened(true);
            }

            // Start a timer to reset frightened state after e.g. 7 seconds
            new java.util.Timer().schedule(new java.util.TimerTask() {
                @Override
                public void run() {
                    for (Ghost ghost : ghosts) {
                        ghost.setFrightened(false);
                    }
                }
            }, 10000);

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


        switch (pacMan.getReqDirection()) {
            case 0:
                if (!getStateOnPosition(pacMan.getxPosition(), pacMan.getyPosition() - 0.1).equals(TileState.WALL)
                        && (!getStateOnPosition(pacMan.getxPosition() + 0.9, pacMan.getyPosition() - 0.1).equals(TileState.WALL))) {
                    setpacManPosition(pacMan.getxPosition(), pacMan.getyPosition() - 0.1);
                    pacMan.updateDirection();
                    return;
                }
                break;
            case 1:
                if (!getStateOnPosition(pacMan.getxPosition() + 1.0, pacMan.getyPosition()).equals(TileState.WALL)
                        && (!getStateOnPosition(pacMan.getxPosition() + 1.0, pacMan.getyPosition() + 0.9).equals(TileState.WALL))) {
                    setpacManPosition(pacMan.getxPosition() + 0.1, pacMan.getyPosition());
                    pacMan.updateDirection();
                    return;
                }
                break;
            case 2:
                if (!getStateOnPosition(pacMan.getxPosition(), pacMan.getyPosition() + 1.0).equals(TileState.WALL)
                        && (!getStateOnPosition(pacMan.getxPosition() + 0.9, pacMan.getyPosition() + 1.0).equals(TileState.WALL))) {
                    setpacManPosition(pacMan.getxPosition(), pacMan.getyPosition() + 0.1);
                    pacMan.updateDirection();
                    return;
                }
                break;
            case 3:
                if (!getStateOnPosition(pacMan.getxPosition() - 0.1, pacMan.getyPosition()).equals(TileState.WALL)
                        && (!getStateOnPosition(pacMan.getxPosition() - 0.1, pacMan.getyPosition() + 0.99).equals(TileState.WALL))) {
                    setpacManPosition(pacMan.getxPosition() - 0.1, pacMan.getyPosition());
                    pacMan.updateDirection();
                    return;
                }
                break;
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
                if(Math.abs(dx) > 1 || Math.abs(dy) > 1) {
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


            if (System.currentTimeMillis() + 2000 > frightenedEndTime[i]) {
                // If the ghost is frightened and the time has not yet ended, start blinking
                blinking[i] = true;
            } else {
                // Otherwise, stop blinking
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
                    JOptionPane.showMessageDialog(frame, "Game Over! Pac-Man was caught.");
                    System.exit(0);
                }
            }

            // Snap to grid to avoid floating-point drift
            ghostX[i] = Math.round(ghostX[i] * 10000.0) / 10000.0;
            ghostY[i] = Math.round(ghostY[i] * 10000.0) / 10000.0;

            System.out.println("Ghost " + i + " Position: (" + ghostX[i] + ", " + ghostY[i] + ") Direction: (" + ghostDirX[i] + ", " + ghostDirY[i] + ")");

            ghosts[i].setPosition((int) Math.round(ghostX[i]), (int) Math.round(ghostY[i]));
            ghostPanels[i].setLocation((int) (ghostX[i] * 40), (int) (ghostY[i] * 40));
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
