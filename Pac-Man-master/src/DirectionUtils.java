public class DirectionUtils {
    /**
     * Converts a direction (0-3) to an angle in degrees.
     * 0 = Up (270 degrees), 1 = Right (0 degrees), 2 = Down (90 degrees), 3 = Left (180 degrees).
     */
    public static double getAngleForDirection(int direction) {
        switch (direction) {
            case 0: return 270;
            case 1: return 0;
            case 2: return 90;
            case 3: return 180;
            default: return 0;
        }
    }
}