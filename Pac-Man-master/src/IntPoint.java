public class IntPoint {
    public final int x, y;
    public IntPoint(int x, int y) { this.x = x; this.y = y; }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IntPoint)) return false;
        IntPoint p = (IntPoint) o;
        return x == p.x && y == p.y;
    }
    @Override
    public int hashCode() { return 31 * x + y; }
}