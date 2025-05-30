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
        return new Point(blinkyPos.x + 2 * vx, blinkyPos.y + 2 * vy);
    }
}