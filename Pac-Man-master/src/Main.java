import java.io.IOException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class Main {
    private static volatile boolean paused = false;

    public static void main(String[] args) {
        Frame frame = null;
        try {
            frame = new Frame();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        frame.setPacManPosition(9, 15);

        // Add key listener for pause/resume
        frame.frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    paused = !paused;
                }
            }
        });

        do {
            long frameStart = System.currentTimeMillis();
            try {
                if (!paused) {
                    frame.movePacMan();
                    frame.moveGhosts();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(Math.max(0, 100 - (System.currentTimeMillis() - frameStart)));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } while (true);
    }
}