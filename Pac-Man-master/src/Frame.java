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

        layeredPane.add(mapPanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(pacManPanel, JLayeredPane.PALETTE_LAYER);
        //layeredPane.add(ghostPanel, JLayeredPane.PALETTE_LAYER);


        String[] ghostImages = {"Orange.png", "Red.png", "Pink.png", "Blue.png"};
        int[][] ghostStarts = {{10, 9}, {9, 8}, {9, 9}, {8, 9}};

        for (int i = 0; i < 4; i++) {

            ghosts[i] = new Ghost(ghostStarts[i][0], ghostStarts[i][1], Ghost.Type.values()[i]);
            ghostPanels[i] = new JPanel();
            ghostPanels[i].setOpaque(false);
            if (i == 0) {
                ghostPanels[i].setBorder(new LineBorder(Color.orange));
            } else if (i == 1) {
                ghostPanels[i].setBorder(new LineBorder(Color.red));
            } else if (i == 2) {
                ghostPanels[i].setBorder(new LineBorder(Color.pink));
            } else if (i == 3) {
                ghostPanels[i].setBorder(new LineBorder(Color.blue));
            }
            ghostPanels[i].setSize(40, 40);
            ghostPanels[i].add(new JLabel(new ImageIcon(ghostImages[i])));
            ghostPanels[i].setLocation(ghostStarts[i][0] * 40, ghostStarts[i][1] * 40);
            ghostX[i] = ghosts[i].getXloc();
            ghostY[i] = ghosts[i].getYloc();
            ghostTargetX[i] = ghosts[i].getXloc();
            ghostTargetY[i] = ghosts[i].getYloc();
            layeredPane.add(ghostPanels[i], JLayeredPane.PALETTE_LAYER);
        }

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
        if (tiles[pacMan.getXloc()][pacMan.getYloc()].getTileState().equals(TileState.DOT)
                || (tiles[pacMan.getXloc()][pacMan.getYloc()].getTileState().equals(TileState.POWER_UP))) {
            tiles[pacMan.getXloc()][pacMan.getYloc()].setTileState(TileState.Empty);
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





    private boolean canMove(double nx, double ny, double size) {
        // Check all four corners of the ghost's bounding box
        return isValid((int)Math.floor(nx), (int)Math.floor(ny), tiles) &&
                isValid((int)Math.floor(nx + size), (int)Math.floor(ny), tiles) &&
                isValid((int)Math.floor(nx), (int)Math.floor(ny + size), tiles) &&
                isValid((int)Math.floor(nx + size), (int)Math.floor(ny + size), tiles);
    }


    private int[][] directions = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};

    private List<Point> findPath(int startX, int startY, int targetX, int targetY) {
        boolean[][] visited = new boolean[tiles.length][tiles[0].length];
        Queue<Point> queue = new LinkedList<>();
        Map<Point, Point> parentMap = new HashMap<>();

        Point start = new Point(startX, startY);
        Point target = new Point(targetX, targetY);

        queue.add(start);
        visited[startX][startY] = true;

        while (!queue.isEmpty()) {
            Point current = queue.poll();
            if (current.equals(target)) {
                return reconstructPath(parentMap, current);
            }
            for (int[] dir : directions) {
                int nx = current.x + dir[0];
                int ny = current.y + dir[1];
                if (isValid(nx, ny, tiles) && !visited[nx][ny]) {
                    visited[nx][ny] = true;
                    Point next = new Point(nx, ny);
                    queue.add(next);
                    parentMap.put(next, current);
                }
            }
        }
        return Collections.emptyList();
    }



    private List<Point> reconstructPath(Map<Point, Point> parentMap, Point target) {
        List<Point> path = new ArrayList<>();
        Point current = target;
        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }
        Collections.reverse(path);
        return path;
    }

    private int[] ghostDirX = new int[4];
    private int[] ghostDirY = new int[4];

    private Point chaseTarget(int ghostIndex) {
        int pacX = pacMan.getXloc();
        int pacY = pacMan.getYloc();
        switch (ghosts[ghostIndex].getType()) {
            case RED: // Blinky: directly chase Pac-Man
                return new Point(pacX, pacY);
            case PINK: // Pinky: 4 tiles ahead of Pac-Man
                int dx = 0, dy = 0;
                switch (pacMan.getDirection()) {
                    case 0: dy = -4; break; // up
                    case 1: dx = 4; break;  // right
                    case 2: dy = 4; break;  // down
                    case 3: dx = -4; break; // left
                }
                return new Point(pacX + dx, pacY + dy);
            case BLUE: // Inky: 2 tiles ahead of Pac-Man (simple version)
                dx = 0; dy = 0;
                switch (pacMan.getDirection()) {
                    case 0: dy = -2; break;
                    case 1: dx = 2; break;
                    case 2: dy = 2; break;
                    case 3: dx = -2; break;
                }
                return new Point(pacX + dx, pacY + dy);
            case ORANGE: // Clyde: chase if far, else scatter to bottom left
                double dist = Math.hypot(ghostX[ghostIndex] - pacX, ghostY[ghostIndex] - pacY);
                if (dist > 8) {
                    return new Point(pacX, pacY);
                } else {
                    return new Point(0, 20); // bottom left corner
                }
            default:
                return new Point(pacX, pacY);
        }
    }

    public void moveGhosts() {
        double ghostSpeed = 0.05;
        double size = 0.95;

        for (int i = 0; i < ghosts.length; i++) {
            int ghostXInt = (int) Math.round(ghostX[i]);
            int ghostYInt = (int) Math.round(ghostY[i]);
            Point target = chaseTarget(i);

            boolean atCenter = Math.abs(ghostX[i] - ghostXInt) < 0.05 && Math.abs(ghostY[i] - ghostYInt) < 0.05;

            if (atCenter) {
                List<Point> path = findPath(ghostXInt, ghostYInt, target.x, target.y);
                if (path != null && path.size() > 1) {
                    Point nextStep = path.get(1);
                    ghostDirX[i] = nextStep.x - ghostXInt;
                    ghostDirY[i] = nextStep.y - ghostYInt;
                }
            }

            double nextX = ghostX[i] + ghostDirX[i] * ghostSpeed;
            double nextY = ghostY[i] + ghostDirY[i] * ghostSpeed;

            if (canMove(nextX, nextY, size)) {
                ghostX[i] = nextX;
                ghostY[i] = nextY;
                ghosts[i].setPosition((int) Math.round(nextX), (int) Math.round(nextY));
                ghostPanels[i].setLocation((int) (nextX * 40), (int) (nextY * 40));
            } else {
                // Try all directions except reverse
                for (int[] dir : directions) {
                    if (dir[0] == -ghostDirX[i] && dir[1] == -ghostDirY[i]) continue;
                    double tryX = ghostX[i] + dir[0] * ghostSpeed;
                    double tryY = ghostY[i] + dir[1] * ghostSpeed;
                    if (canMove(tryX, tryY, size)) {
                        ghostDirX[i] = dir[0];
                        ghostDirY[i] = dir[1];
                        ghostX[i] = tryX;
                        ghostY[i] = tryY;
                        ghosts[i].setPosition((int) Math.round(tryX), (int) Math.round(tryY));
                        ghostPanels[i].setLocation((int) (tryX * 40), (int) (tryY * 40));
                        break;
                    }
                }
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
