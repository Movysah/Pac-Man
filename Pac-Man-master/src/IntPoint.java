/**
 * Represents a point with integer coordinates.
 */
public class IntPoint {

    public final int x;
    public final int y;

    /**
     * Constructs an IntPoint with the specific coordinates.
     */
    public IntPoint(int x, int y) { this.x = x; this.y = y; }

    /**
     * Checks if this point is equal to another object.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IntPoint)) return false;
        IntPoint p = (IntPoint) o;
        return x == p.x && y == p.y;
    }

    /**
     * Returns a hash code for this point.
     */

    @Override
    public int hashCode() { return 31 * x + y; }
}