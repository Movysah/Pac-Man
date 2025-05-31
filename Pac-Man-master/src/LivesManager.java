/**
 * Manages the number of lives in the Pac-Man game.
 * Provides methods to add lives, retrieve the current number of lives,
 * and get a formatted string representation of the lives.
 */
public class LivesManager {
    /** The current number of lives. Initialized from GameConstants. */
    private int lives = GameConstants.INITIAL_LIVES;

    /**
     * Adds the specified number of lives to the current total.
     *
     * @param delta the number of lives to add (can be negative to subtract lives)
     */
    public void addLives(int delta) {
        lives += delta;
    }

    /**
     * Returns the current number of lives.
     *
     * @return the number of lives
     */
    public int getLives() {
        return lives;
    }

    /**
     * Returns a string representation of the current number of lives.
     */
    public String getLivesText() {
        return "Lives: " + lives;
    }
}