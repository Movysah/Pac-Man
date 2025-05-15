public class PacMan {


    private int[] location; // in tiles

    private double xPosition; // an xact position
    private double yPosition; // an exact location

    private int direction; // 0 - up  1 - right  2 - down  3 - left
    private int reqDirection;


    public PacMan() {
        location = new int[2];
        reqDirection = 1;

    }

    public void updateLocation() {
        location[0] = (int) (xPosition+0.5);
        location[1] = (int) (yPosition+0.5);
    }




    public int[] getLocation() {
        return location;
    }

    public void setLocation(int[] location) {
        this.location = location;
    }

    public int getXloc() {
        return location[0];
    }

    public int getYloc() {
        return location[1];
    }

    public int getDirection() {
        return direction;
    }

    public void updateDirection() {
        direction = reqDirection;
    }

    public int getReqDirection() {
        return reqDirection;
    }

    public void setReqDirection(short reqDirection) {
        this.reqDirection = reqDirection;
    }

    public double getxPosition() {
        return xPosition;
    }

    public void setxPosition(double xPosition) {
        this.xPosition = Math.round(xPosition * 100) / 100.0;
    }

    public double getyPosition() {
        return yPosition;
    }

    public void setyPosition(double yPosition) {
        this.yPosition = Math.round(yPosition * 100) / 100.0;
    }
}
