import java.awt.*;

public class RedGhost extends Ghost {
    public RedGhost(double x, double y) { super(x, y); }

    @Override
    public Point getChaseTarget(Tile[][] tiles, Point pacmanPos, int pacmanDir, Point blinkyPos) {
        return pacmanPos;
    }
}