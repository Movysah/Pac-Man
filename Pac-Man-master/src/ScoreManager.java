public class ScoreManager {

    private int score = 0;

    /**
     * Adds the specific number of points to the current score.
     */
    public void addScore(int points) {
        score += points;
    }

    /**
     * Returns the current score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Resets the score to zero.
     */
    public void resetScore() {
        score = 0;
    }
}