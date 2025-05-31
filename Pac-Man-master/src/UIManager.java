import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class UIManager {
    private Frame frame;
    private JLabel scoreLabel;
    private JLabel livesLabel;
    private JLabel controlsLabel;
    private ScoreManager scoreManager;
    private LivesManager livesManager;
    private JDialog dialog;
    private int score;
    private JLabel message;

    public UIManager(Frame frame, ScoreManager scoreManager, LivesManager livesManager) {
        this.frame = frame;
        this.scoreManager = scoreManager;
        this.livesManager = livesManager;
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
        scoreLabel.setBounds(GameConstants.MAP_WIDTH, 10, frame.frame.getWidth() - GameConstants.MAP_WIDTH, 30);
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);

        livesLabel = new JLabel(livesManager.getLivesText());
        livesLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        livesLabel.setBounds(GameConstants.MAP_WIDTH + 20, 50, frame.frame.getWidth() - GameConstants.MAP_WIDTH, 30);
        livesLabel.setForeground(java.awt.Color.RED);

        controlsLabel = new JLabel();
        controlsLabel.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
        controlsLabel.setText("<html>Controls:<br>W = Up<br>A = Left<br>S = Down<br>D = Right<br>Pause = P</html>");
        controlsLabel.setBounds(GameConstants.MAP_WIDTH + 20, 80, frame.frame.getWidth() - GameConstants.MAP_WIDTH, 150);

        dialog = new JDialog();
        dialog.setTitle("Game Over");
        dialog.setModal(true);
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);



        JPanel buttonPanel = new JPanel();
        JButton exitButton = new JButton("Exit");


        exitButton.addActionListener((ActionEvent e) -> {
            dialog.dispose();
            System.exit(0);
        });


        buttonPanel.add(exitButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(false);
    }


    public void showGameOverDialog(int score) {
        message = new JLabel("<html><center><h1>Game Over!</h1><br>Your Score: " + scoreManager.getScore() + "</center></html>", SwingConstants.CENTER);
        dialog.add(message, BorderLayout.CENTER);
        dialog.setVisible(true);
    }


    public void addUIComponents(JLayeredPane layeredPane) {
        layeredPane.add(scoreLabel, JLayeredPane.MODAL_LAYER);
        layeredPane.add(livesLabel, JLayeredPane.MODAL_LAYER);
        layeredPane.add(controlsLabel, JLayeredPane.MODAL_LAYER);
    }

    public void updateScoreLabel() {
        score = scoreManager.getScore();
        scoreLabel.setText("Score: " + score);
    }

    public void updateLivesLabel() {
        livesLabel.setText(livesManager.getLivesText());
    }

    public void showLevelCompleteDialog(int score) {
        JOptionPane.showMessageDialog(frame.frame, "Level Complete!\nScore: " + score);
    }


}