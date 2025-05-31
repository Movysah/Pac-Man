import java.awt.*;

public class RedGhost extends Ghost {
    public RedGhost(double x, double y) { super(x, y); }

    /**
     * Returns the target position for the Red ghost in chase mode.
     */
    @Override
    public Point getChaseTarget(Tile[][] tiles, Point pacmanPos, int pacmanDir, Point blinkyPos) {
        return pacmanPos;
    }
}