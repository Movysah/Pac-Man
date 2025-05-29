import java.awt.Point;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

public class Ghost {
    public int getDirection() {
        return direction;
    }

    public enum Type { BLINKY, PINKY, INKY, CLYDE }
    private double x, y;
    private int direction;
    private Type ghostType;

    public Ghost(double x, double y, Type type) {
        this.x = x;
        this.y = y;
        this.ghostType = type;
    }

    public int getXloc() { return (int) x; }
    public int getYloc() { return (int) y; }
    public void setPosition(double x, double y) { this.x = x; this.y = y; }
    public Type getType() { return ghostType; }

    // Main pathfinding method
    public Point getNextMove(Tile[][] tiles, Point pacmanPos, int pacmanDir, Point blinkyPos) {
        Point target = getChaseTarget(tiles, pacmanPos, pacmanDir, blinkyPos);
        return bfsNextStep(tiles, new Point(getXloc(), getYloc()), target);
    }

    // Chasing pattern logic
    private Point getChaseTarget(Tile[][] tiles, Point pacmanPos, int pacmanDir, Point blinkyPos) {
        int[] dx = {0, 1, 0, -1};
        int[] dy = {-1, 0, 1, 0};
        switch (ghostType) {
            case BLINKY:
                return pacmanPos;
            case PINKY:
                // 4 tiles ahead of Pac-Man
                return new Point(
                        pacmanPos.x + 4 * dx[pacmanDir],
                        pacmanPos.y + 4 * dy[pacmanDir]
                );
            case INKY:
                // Vector from Blinky through 2 tiles ahead of Pac-Man
                int px = pacmanPos.x + 2 * dx[pacmanDir];
                int py = pacmanPos.y + 2 * dy[pacmanDir];
                int vx = px - blinkyPos.x;
                int vy = py - blinkyPos.y;
                return new Point(blinkyPos.x + 2 * vx, blinkyPos.y + 2 * vy);
            case CLYDE:
                // If farther than 8 tiles, chase Pac-Man; else, go to bottom-left corner
                int dist = Math.abs(getXloc() - pacmanPos.x) + Math.abs(getYloc() - pacmanPos.y);
                if (dist > 8) return pacmanPos;
                return new Point(0, tiles[0].length - 1); // bottom-left
            default:
                return pacmanPos;
        }
    }

    // BFS for next step
    private Point bfsNextStep(Tile[][] tiles, Point start, Point target) {
        Queue<java.util.List<Point>> queue = new LinkedList<>();
        Set<Point> visited = new HashSet<>();
        queue.add(Arrays.asList(start));
        visited.add(start);

        while (!queue.isEmpty()) {
            java.util.List<Point> path = queue.poll();
            Point current = path.get(path.size() - 1);

            if (current.equals(target)) {
                if (path.size() > 1) return path.get(1);
                else return current;
            }

            for (Point dir : Arrays.asList(
                    new Point(0, -1), new Point(1, 0),
                    new Point(0, 1), new Point(-1, 0))) {
                Point next = new Point(current.x + dir.x, current.y + dir.y);
                if (isValid(next, tiles) && !visited.contains(next)) {
                    visited.add(next);
                    java.util.List<Point> newPath = new java.util.ArrayList<>(path);
                    newPath.add(next);
                    queue.add(newPath);
                }
            }
        }
        return start;
    }

    private boolean isValid(Point p, Tile[][] tiles) {
        int x = p.x, y = p.y;
        return x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length
                && tiles[x][y].getTileState() != TileState.WALL;
    }
}