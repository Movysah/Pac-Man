
import java.awt.*;

public class BlueGhost extends Ghost {
    public BlueGhost(double x, double y) { super(x, y); }

    @Override
    public Point getChaseTarget(Tile[][] tiles, Point pacmanPos, int pacmanDir, Point blinkyPos) {
        int[] dx = {0, 1, 0, -1};
        int[] dy = {-1, 0, 1, 0};
        int px = pacmanPos.x + 2 * dx[pacmanDir];
        int py = pacmanPos.y + 2 * dy[pacmanDir];
        int vx = px - blinkyPos.x;
        int vy = py - blinkyPos.y;

        int tx = blinkyPos.x + 2 * vx;
        int ty = blinkyPos.y + 2 * vy;

        // Clamp to map bounds
        int maxX = tiles.length - 1;
        int maxY = tiles[0].length - 1;
        tx = Math.max(0, Math.min(tx, maxX));
        ty = Math.max(0, Math.min(ty, maxY));


        if (!isValid(tx, ty, tiles)) {
            double minDist = Double.MAX_VALUE;
            Point best = new Point(blinkyPos.x, blinkyPos.y);
            for (int x = 0; x <= maxX; x++) {
                for (int y = 0; y <= maxY; y++) {
                    if (isValid(x, y, tiles)) {
                        double dist = Math.hypot(x - tx, y - ty);
                        if (dist < minDist) {
                            minDist = dist;
                            best = new Point(x, y);
                        }
                    }
                }
            }
            return best;
        }

        return new Point(tx, ty);
    }

    // Helper to check if a tile is valid (not a wall)
    private boolean isValid(int x, int y, Tile[][] tiles) {
        return x >= 0 && y >= 0 && x < tiles.length && y < tiles[0].length && !tiles[x][y].getTileState().equals(TileState.WALL);
    }
}