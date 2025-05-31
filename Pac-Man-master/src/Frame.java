import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Frame implements KeyListener {
    JFrame frame;
    JPanel mapPanel;
    JPanel pacManPanel;
    JLabel pacManLabel;
    PacMan pacMan;
    GhostManager ghostManager;
    Tile[][] tiles;
    ScoreManager scoreManager;
    LivesManager livesManager;
    UIManager uiManager;
    AnimationManager animationManager;
    ControlManager controlManager;
    GameStateManager gameStateManager;



    public Frame() throws IOException {
        frame = new JFrame();
        frame.setSize(GameConstants.FRAME_WIDTH, GameConstants.FRAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.addKeyListener(this);

        mapPanel = new JPanel();
        mapPanel.setSize(GameConstants.MAP_WIDTH, GameConstants.MAP_HEIGHT);
        mapPanel.setBounds(0, 0, GameConstants.MAP_WIDTH, GameConstants.MAP_HEIGHT);
        mapPanel.setLayout(new GridLayout(GameConstants.ROWS, GameConstants.COLS));

        tiles = new Tile[GameConstants.COLS][GameConstants.ROWS];
        MapLoad mapLoad = new MapLoad();
        for (int i = 0; i < GameConstants.COLS * GameConstants.ROWS; i++) {
            JPanel panel = new JPanel();
            int x = i % GameConstants.COLS;
            int y = i / GameConstants.COLS;
            tiles[x][y] = new Tile(panel);
            tiles[x][y].setTileState(mapLoad.steteOfTile(x, y));
            tiles[x][y].updateToState();
            mapPanel.add(panel);
        }

        pacManPanel = new JPanel();
        pacManPanel.setLayout(null);
        pacManPanel.setBounds(0, 0, 40, 40);
        pacManPanel.setOpaque(false);

        pacManLabel = new JLabel();
        pacManLabel.setBounds(0, 0, 40, 40);
        pacManLabel.setBackground(new Color(0, 0, 0));
        pacManLabel.setOpaque(false);
        pacManPanel.add(pacManLabel);

        pacMan = new PacMan();

        scoreManager = new ScoreManager();
        livesManager = new LivesManager();
        uiManager = new UIManager(this, scoreManager, livesManager);
        animationManager = new AnimationManager(this, pacManLabel);
        controlManager = new ControlManager(pacMan);


        gameStateManager = new GameStateManager();



        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.add(mapPanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(pacManPanel, JLayeredPane.PALETTE_LAYER);
        uiManager.addUIComponents(layeredPane);
        ghostManager = new GhostManager(this, tiles, pacMan, layeredPane);

        frame.setLayeredPane(layeredPane);
        frame.setVisible(true);
    }

    public void setPacManPosition(double x, double y) {
        pacManPanel.setLocation((int) (40 * x), (int) (40 * y));
        pacMan.setxPosition(x);
        pacMan.setyPosition(y);
    }

    public void updateScore(int points) {
        scoreManager.addScore(points);
        uiManager.updateScoreLabel();
    }

    public void updateLives(int delta) {
        livesManager.addLives(delta);
        uiManager.updateLivesLabel();
        if (livesManager.getLives() <= 0) {
            System.out.println("103");
            onGameOver();
        }
    }

    public void movePacMan() {
        pacMan.move(tiles, this);
        animationManager.animatePacManMouth(pacMan.getDirection());
    }

    public void moveGhosts() {
        ghostManager.moveGhosts();
    }

    public void onLevelComplete() {
        gameStateManager.setState(GameState.LEVEL_COMPLETE);
        uiManager.showLevelCompleteDialog(scoreManager.getScore());
        System.exit(0);
    }

    public void onGameOver() {
        gameStateManager.setState(GameState.GAME_OVER);
        uiManager.showGameOverDialog(scoreManager.getScore());
        frame.dispose();

    }





    public String getLivesText() {
        return livesManager.getLivesText();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        controlManager.handleKeyPress(e);
    }
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}


    public int getLives() {
        return livesManager.getLives();
    }
}