import java.awt.Point;
import java.util.*;

public abstract class Ghost {
    protected double x, y;
    protected int direction;
    protected boolean frightened = false;

    public Ghost(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public int getXloc() { return (int) x; }
    public int getYloc() { return (int) y; }
    public void setPosition(double x, double y) { this.x = x; this.y = y; }
    public int getDirection() { return direction; }
    public void setFrightened(boolean frightened) { this.frightened = frightened; }
    public boolean isFrightened() { return frightened; }
    protected IntPoint prevPosition = null;

    // Abstract: each ghost implements its own targeting logic
    public abstract Point getChaseTarget(Tile[][] tiles, Point pacmanPos, int pacmanDir, Point blinkyPos);

    // Shared BFS logic
    public Point getNextMove(Tile[][] tiles, Point pacmanPos, int pacmanDir, Point blinkyPos) {
        Point target = getChaseTarget(tiles, pacmanPos, pacmanDir, blinkyPos);
        IntPoint start = new IntPoint(getXloc(), getYloc());
        IntPoint intTarget = new IntPoint(target.x, target.y);
        IntPoint nextStep = bfsNextStep(tiles, start, intTarget, prevPosition);
        prevPosition = start; // Update previous position for next move
        return new Point(nextStep.x, nextStep.y);
    }


    // Chasing pattern logic


    // BFS for next step
    private IntPoint bfsNextStep(Tile[][] tiles, IntPoint start, IntPoint target, IntPoint prev) {
        Queue<IntPoint> queue = new LinkedList<>();
        Set<IntPoint> visited = new HashSet<>();
        Map<IntPoint, IntPoint> parent = new HashMap<>();
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            IntPoint current = queue.poll();

            if (current.equals(target)) {
                List<IntPoint> path = new ArrayList<>();
                for (IntPoint step = target; step != null; step = parent.get(step))
                    path.add(0, step);
                if (path.size() > 1) return path.get(1);
                else return current;
            }

            for (IntPoint dir : Arrays.asList(
                    new IntPoint(0, -1), new IntPoint(1, 0),
                    new IntPoint(0, 1), new IntPoint(-1, 0))) {
                IntPoint next = new IntPoint(current.x + dir.x, current.y + dir.y);
                if (isValid(next, tiles) && !visited.contains(next) && (prev == null || !next.equals(prev))) {
                    visited.add(next);
                    parent.put(next, current);
                    queue.add(next);
                }
            }
        //    System.out.println("BFS Start: " + start + ", Target: " + target);
         //   System.out.println("Visited: " + visited);


        }
        return start;
    }

    private boolean isValid(IntPoint p, Tile[][] tiles) {
        int x = p.x, y = p.y;
        return x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length
                && tiles[x][y].getTileState() != TileState.WALL;
    }

    private boolean isValid(Point p, Tile[][] tiles) {
        int x = p.x, y = p.y;
        return x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length
                && tiles[x][y].getTileState() != TileState.WALL;
    }



}