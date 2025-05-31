import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class AnimationManager {
    private Frame frame;
    private JLabel pacManLabel;
    private BufferedImage pacmanBase;
    private BufferedImage pacmanClosedBase;
    private boolean pacmanMouthOpen = true;
    private int pacmanMouthFrame = 0;
    private final int PACMAN_MOUTH_ANIM_RATE = 3;

    public AnimationManager(Frame frame, JLabel pacManLabel) throws IOException {
        this.frame = frame;
        this.pacManLabel = pacManLabel;
        pacmanBase = javax.imageio.ImageIO.read(new File("Pac-Man.png"));
        pacmanClosedBase = javax.imageio.ImageIO.read(new File("Pac-Man_Closed.png"));
    }

    public void animatePacManMouth(int direction) {
        pacmanMouthFrame++;
        if (pacmanMouthFrame >= PACMAN_MOUTH_ANIM_RATE) {
            double angle = DirectionUtils.getAngleForDirection(direction);
            BufferedImage base = pacmanMouthOpen ? pacmanBase : pacmanClosedBase;
            BufferedImage rotated = AnimationUtils.rotateImage(base, angle);
            pacManLabel.setIcon(new ImageIcon(rotated));
            pacmanMouthOpen = !pacmanMouthOpen;
            pacmanMouthFrame = 0;
        }
    }
}