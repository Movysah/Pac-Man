public class LivesManager {
    private int lives = GameConstants.INITIAL_LIVES;

    public void addLives(int delta) {
        lives += delta;
    }

    public int getLives() {
        return lives;
    }



    public String getLivesText() {
        return "Lives: " + lives;
    }
}