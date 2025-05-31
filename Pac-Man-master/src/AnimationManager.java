import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class AnimationManager {
    private Frame frame;
    private JLabel pacManLabel;
    private BufferedImage pacmanBase;
    private BufferedImage pacmanClosedBase;
    private boolean pacmanMouthOpen = true;
    private int pacmanMouthFrame = 0;
    private final int PACMAN_MOUTH_ANIM_RATE = 3;

    /**
     * Constructs an AnimationManager for Pac-Man animations.
     */
    public AnimationManager(Frame frame, JLabel pacManLabel) throws IOException {
        this.frame = frame;
        this.pacManLabel = pacManLabel;
        pacmanBase = javax.imageio.ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Pac-Man.png")));
        pacmanClosedBase = javax.imageio.ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Pac-Man_Closed.png")));
    }

    /**
     * Animation for Pac-Man's mouth opening and closing based on the direction of movement.
     */
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