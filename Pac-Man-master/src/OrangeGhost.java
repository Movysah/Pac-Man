import java.awt.*;

public class OrangeGhost extends Ghost {
    public OrangeGhost(double x, double y) { super(x, y); }

    @Override
    public Point getChaseTarget(Tile[][] tiles, Point pacmanPos, int pacmanDir, Point blinkyPos) {
        int dist = Math.abs(getXloc() - pacmanPos.x) + Math.abs(getYloc() - pacmanPos.y);
        if (dist > 8) return pacmanPos;
        return new Point(1, 19);
    }

}
