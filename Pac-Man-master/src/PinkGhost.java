import java.awt.*;

public class PinkGhost extends Ghost {
    public PinkGhost(double x, double y) { super(x, y); }

    @Override
    public Point getChaseTarget(Tile[][] tiles, Point pacmanPos, int pacmanDir, Point blinkyPos) {
        int[] dx = {0, 1, 0, -1};
        int[] dy = {-1, 0, 1, 0};
        return new Point(
                pacmanPos.x + 2 * dx[pacmanDir],
                pacmanPos.y + 2 * dy[pacmanDir]
        );
    }
}