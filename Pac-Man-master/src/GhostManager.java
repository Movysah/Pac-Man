import javax.swing.*;
import java.awt.*;

public class GhostManager {
    private Ghost[] ghosts = new Ghost[4];
    private JPanel[] ghostPanels = new JPanel[4];
    private double[] ghostX = new double[4], ghostY = new double[4];
    private int[] ghostDirX = new int[4], ghostDirY = new int[4];
    private long[] frightenedEndTime = new long[4];
    private boolean[] blinking = new boolean[4];
    private final String[] ghostImages = {"out/production/PacManZaverecka/Orange.png", "out/production/PacManZaverecka/Red.png", "out/production/PacManZaverecka/Pink.png", "out/production/PacManZaverecka/Blue.png"};
    private final Point[] ghostCorners = {
            new Point(0, 0), new Point(18, 0), new Point(0, 20), new Point(18, 20)
    };
    private Tile[][] tiles;
    private PacMan pacMan;
    private Frame frame;

    /**
     * Constructs a GhostManager to manage the ghosts in the game.
     */
    public GhostManager(Frame frame, Tile[][] tiles, PacMan pacMan, JLayeredPane layeredPane) {
        this.frame = frame;
        this.tiles = tiles;
        this.pacMan = pacMan;
        int[][] ghostStarts = {{10, 9}, {9, 8}, {9, 9}, {8, 9}};
        for (int i = 0; i < 4; i++) {
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
            ghostPanels[i] = new JPanel();
            ghostPanels[i].setOpaque(false);
            ghostPanels[i].setBounds(ghostStarts[i][0] * 40, ghostStarts[i][1] * 40, 40, 40);
            JLabel ghostLabel = new JLabel(new ImageIcon(ghostImages[i]));
            ghostPanels[i].add(ghostLabel);
            ghostX[i] = ghostStarts[i][0];
            ghostY[i] = ghostStarts[i][1];
            frame.frame.getLayeredPane().add(ghostPanels[i], JLayeredPane.PALETTE_LAYER);
            layeredPane.add(ghostPanels[i], JLayeredPane.PALETTE_LAYER);
        }
    }

    /**
     * Moves all ghosts based on their current state and position.
     */
    public void moveGhosts() {
        double ghostSpeed = 0.083333333333333333;
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
            if (atCenter) {
                Point nextStep;
                if (ghosts[i].isFrightened()) {
                    Point target = ghostCorners[i];
                    int bestDx = 0, bestDy = 0, minDist = Integer.MAX_VALUE;
                    int[][] directions = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
                    for (int[] dir : directions) {
                        int nx = ghostXInt + dir[0], ny = ghostYInt + dir[1];
                        if (isValid(nx, ny)) {
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
                int dx = nextStep.x - ghostXInt, dy = nextStep.y - ghostYInt;
                if (Math.abs(dx) > 1 || Math.abs(dy) > 1) {
                    dx = 0;
                    dy = 0;
                }
                ghostDirX[i] = dx;
                ghostDirY[i] = dy;
            }
            double nextX = ghostX[i] + ghostDirX[i] * ghostSpeed;
            double nextY = ghostY[i] + ghostDirY[i] * ghostSpeed;
            int nextTileX = (int) Math.floor(nextX + 0.00 * ghostDirX[i]);
            int nextTileY = (int) Math.floor(nextY + 0.00 * ghostDirY[i]);
            if (isValid(nextTileX, nextTileY)) {
                ghostX[i] = nextX;
                ghostY[i] = nextY;
            }
            long timeLeft = frightenedEndTime[i] - System.currentTimeMillis();
            blinking[i] = (timeLeft <= 2000 && timeLeft > 0);
            if (timeLeft <= 0 && ghosts[i].isFrightened()) {
                ghosts[i].setFrightened(false);
                blinking[i] = false;
            }
            JLabel ghostLabel = (JLabel) ghostPanels[i].getComponent(0);
            if (ghosts[i].isFrightened()) {
                if (blinking[i] && (System.currentTimeMillis() / 200) % 2 == 0) {
                    ghostLabel.setIcon(new ImageIcon(ghostImages[i]));
                } else {
                    ghostLabel.setIcon(new ImageIcon("out/production/PacManZaverecka/frightened.png"));
                }
            } else {
                ghostLabel.setIcon(new ImageIcon(ghostImages[i]));
            }
            double dist = Math.hypot(ghostX[i] - pacMan.getxPosition(), ghostY[i] - pacMan.getyPosition());
            if (ghosts[i].isFrightened() && dist < 0.9) {
                frame.updateScore(100);
                resetGhost(i);
                continue;
            } else if (!ghosts[i].isFrightened() && dist < 0.9) {
                frame.updateLives(-1);
                if (frame.getLivesText().endsWith("0")) {
                    System.out.println("114");
                    frame.onGameOver();
                } else {
                    resetAll();
                }
                continue;
            }
            ghostX[i] = Math.round(ghostX[i] * 1000.0) / 1000.0;
            ghostY[i] = Math.round(ghostY[i] * 1000.0) / 1000.;
            ghosts[i].setPosition((int) Math.round(ghostX[i]), (int) Math.round(ghostY[i]));
            System.out.println("Ghost " + i + " position: (" + ghostX[i] + ", " + ghostY[i] + ")");
            ghostPanels[i].setLocation((int) (ghostX[i] * 40), (int) (ghostY[i] * 40));
        }
    }

    /**
     * Frightens all ghosts for 10 seconds.
     */
    public void frightenGhosts() {
        for (int i = 0; i < ghosts.length; i++) {
            ghosts[i].setFrightened(true);
            frightenedEndTime[i] = System.currentTimeMillis() + 10000;
            blinking[i] = false;
        }
    }

    /**
     * Resets the position and state of a specific ghost.
     */
    private void resetGhost(int i) {
        int[][] ghostStarts = {{10, 9}, {9, 8}, {9, 9}, {8, 9}};
        ghostX[i] = ghostStarts[i][0];
        ghostY[i] = ghostStarts[i][1];
        ghostDirX[i] = 0;
        ghostDirY[i] = 0;
        ghosts[i].setFrightened(false);
        ghostPanels[i].setLocation((int) (ghostX[i] * 40), (int) (ghostY[i] * 40));
    }

    /**
     * Resets all ghosts to their starting positions and states.
     */
    protected void resetAll() {
        int[][] ghostStarts = {{10, 9}, {9, 8}, {9, 9}, {8, 9}};
        pacMan.setxPosition(9);
        pacMan.setyPosition(15);
        pacMan.setDirection(0); // 0 = right
        for (int j = 0; j < ghosts.length; j++) {
            ghostX[j] = ghostStarts[j][0];
            ghostY[j] = ghostStarts[j][1];
            ghostDirX[j] = 0;
            ghostDirY[j] = 0;
            ghosts[j].setFrightened(false);
            ghostPanels[j].setLocation((int) (ghostX[j] * 40), (int) (ghostY[j] * 40));
        }
    }

    /**
     * Checks if the given coordinates are valid (not a wall).
     */
    private boolean isValid(int x, int y) {
        return x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length && tiles[x][y].getTileState() != TileState.WALL;
    }


    /**
     * Returns the array of ghosts managed by this GhostManager.
     */
    public Ghost[] getGhosts() {
        return ghosts;
    }
}