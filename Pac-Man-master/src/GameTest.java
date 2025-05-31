
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.awt.*;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GameTest {

    @Test
    void testPinkGhostChaseTarget() {
        PinkGhost pink = new PinkGhost(5, 5);
        Tile[][] tiles = new Tile[20][20];
        Point pacmanPos = new Point(10, 10);
        int pacmanDir = 0;
        Point blinkyPos = new Point(0, 0);
        Point target = pink.getChaseTarget(tiles, pacmanPos, pacmanDir, blinkyPos);
        assertEquals(new Point(12, 10), target);
    }

    @Test
    void testLivesDecreaseOnGhostCollision() {
        Frame frame = null;
        try {
            frame = new Frame();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int initialLives = frame.getLives();
        frame.updateLives(-1);
        assertEquals(initialLives - 1, frame.getLives());
    }




    @Test
    void testPacManDirectionResetAfterCaught() {
        PacMan pacMan = new PacMan();
        pacMan.setDirection(2);
        pacMan.setDirection(0);
        assertEquals(0, pacMan.getDirection());
    }


    @Test
    void testGhostsFrightenedStateResetOnResetAll() throws IOException {
        Frame frame = new Frame();
        GhostManager manager = new GhostManager(frame, new Tile[20][20], new PacMan(), new javax.swing.JLayeredPane());
        for (Ghost ghost : manager.getGhosts()) {
            ghost.setFrightened(true);
        }
        manager.resetAll();
        for (Ghost ghost : manager.getGhosts()) {
            Assertions.assertFalse(ghost.isFrightened());
        }
    }



    @Test
    void testLivesDoNotGoBelowZero() throws IOException {
        Frame frame = new Frame();
        while (frame.getLives() > 0) {
            frame.updateLives(-1);
        }
        frame.updateLives(-1); // try to go below zero
        Assertions.assertEquals(0, frame.getLives());
    }






}