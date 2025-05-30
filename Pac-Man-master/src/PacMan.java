public class PacMan {
    private short direction = 1; // 0=up, 1=right, 2=down, 3=left
    private short reqDirection = 1;
    private double xPosition = 9, yPosition = 15;





    public void setReqDirection(short dir) {
        reqDirection = dir;
    }

    public short getReqDirection() {
        return reqDirection;
    }

    public short getDirection() {
        return direction;
    }

    public void updateDirection() {
        direction = reqDirection;
    }

    public double getxPosition() {
        return xPosition;
    }

    public double getyPosition() {
        return yPosition;
    }

    public void setxPosition(double x) {
        xPosition = x;
    }

    public void setyPosition(double y) {
        yPosition = y;
    }

    public void updateLocation() { /* update grid location if needed */ }

    public int getXloc() {
        return (int) Math.round(xPosition);
    }

    public int getYloc() {
        return (int) Math.round(yPosition);
    }
}