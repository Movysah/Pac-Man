public class GameStateManager {
    private GameState state = GameState.RUNNING;

    public void setState(GameState state) {
        this.state = state;
    }

    public GameState getState() {
        return state;
    }
}