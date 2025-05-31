
public class PacMan {
    private short direction = 1;
    private short reqDirection = 1;
    private double xPosition = GameConstants.PACMAN_START_X, yPosition = GameConstants.PACMAN_START_Y;

    public void setReqDirection(short dir) {
        reqDirection = dir;
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

    public int getXloc() {
        return (int) Math.round(xPosition);
    }

    public int getYloc() {
        return (int) Math.round(yPosition);
    }


    /**
     * Moves the Pac-Man based on the requested direction.
     */
    public void move(Tile[][] tiles, Frame frame) {
        boolean canChangeDirection = false;
        switch (reqDirection) {
            case 0:
                if (canMove(tiles, xPosition, yPosition - 0.1, xPosition + 0.9, yPosition - 0.1)) {
                    setPosition(xPosition, yPosition - 0.1, frame);
                    updateDirection();
                    canChangeDirection = true;
                }
                break;
            case 1:
                if (canMove(tiles, xPosition + 1.0, yPosition, xPosition + 1.0, yPosition + 0.9)) {
                    setPosition(xPosition + 0.1, yPosition, frame);
                    updateDirection();
                    canChangeDirection = true;
                }
                break;
            case 2:
                if (canMove(tiles, xPosition, yPosition + 1.0, xPosition + 0.9, yPosition + 1.0)) {
                    setPosition(xPosition, yPosition + 0.1, frame);
                    updateDirection();
                    canChangeDirection = true;
                }
                break;
            case 3:
                if (canMove(tiles, xPosition - 0.1, yPosition, xPosition - 0.1, yPosition + 0.99)) {
                    setPosition(xPosition - 0.1, yPosition, frame);
                    updateDirection();
                    canChangeDirection = true;
                }
                break;
        }
        if (!canChangeDirection) {
            switch (direction) {
                case 0:
                    if (canMove(tiles, xPosition, yPosition - 0.1, xPosition + 0.9, yPosition - 0.1))
                        setPosition(xPosition, yPosition - 0.1, frame);
                    break;
                case 1:
                    if (canMove(tiles, xPosition + 1.0, yPosition, xPosition + 1.0, yPosition + 0.9))
                        setPosition(xPosition + 0.1, yPosition, frame);
                    break;
                case 2:
                    if (canMove(tiles, xPosition, yPosition + 1.0, xPosition + 0.9, yPosition + 1.0))
                        setPosition(xPosition, yPosition + 0.1, frame);
                    break;
                case 3:
                    if (canMove(tiles, xPosition - 0.1, yPosition, xPosition - 0.1, yPosition + 0.9))
                        setPosition(xPosition - 0.1, yPosition, frame);
                    break;
            }
        }
        eat(tiles, frame);
        if (allDotsCleared(tiles)) {
            frame.onLevelComplete();
        }
    }

    /**
     * Checks if Pac-Man can move to the new position based on the tile state.
     */
    private boolean canMove(Tile[][] tiles, double x1, double y1, double x2, double y2) {
        return getStateOnPosition(tiles, x1, y1) != TileState.WALL &&
                getStateOnPosition(tiles, x2, y2) != TileState.WALL;
    }

    /**
     * Gets the state of the tile at the given position.
     */
    private TileState getStateOnPosition(Tile[][] tiles, double xpos, double ypos) {
        return tiles[(int) xpos][(int) ypos].getTileState();
    }

    /**
     * Sets the position of Pac-Man and updates the frame.
     */
    private void setPosition(double x, double y, Frame frame) {
        frame.setPacManPosition(x, y);
        setxPosition(x);
        setyPosition(y);
    }


    /**
     * Eats the dot or power-up at the current position and updates the score.
     */
    private void eat(Tile[][] tiles, Frame frame) {
        int x = getXloc(), y = getYloc();
        if (tiles[x][y].getTileState() == TileState.DOT) {
            tiles[x][y].setTileState(TileState.Empty);
            frame.updateScore(GameConstants.DOT_SCORE);
        } else if (tiles[x][y].getTileState() == TileState.POWER_UP) {
            tiles[x][y].setTileState(TileState.Empty);
            frame.updateScore(GameConstants.POWER_UP_SCORE);
            frame.ghostManager.frightenGhosts();
        }
    }


    /**
     * Checks if all dots are cleared from the board.
     */
    private boolean allDotsCleared(Tile[][] tiles) {
        for (int i = 0; i < tiles.length; i++)
            for (int j = 0; j < tiles[0].length; j++)
                if (tiles[i][j].getTileState() == TileState.DOT)
                    return false;
        return true;
    }


    /**
     * Sets the direction
     */
    public void setDirection(int dir) {
        this.direction = (short) dir;
    }

}