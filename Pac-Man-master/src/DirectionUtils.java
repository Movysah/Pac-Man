public class DirectionUtils {
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