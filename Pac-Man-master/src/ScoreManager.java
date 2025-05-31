public class ScoreManager {
    private int score = 0;

    public void addScore(int points) {
        score += points;
    }

    public int getScore() {
        return score;
    }

    public void resetScore() {
        score = 0;
    }
}