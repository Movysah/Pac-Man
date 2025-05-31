import java.awt.event.KeyEvent;

public class ControlManager {
    private PacMan pacMan;

    public ControlManager(PacMan pacMan) {
        this.pacMan = pacMan;
    }

    /**
     * Handles key presses to
     */
    public void handleKeyPress(KeyEvent e) {
        switch (e.getKeyCode()) {
            case 87:
                pacMan.setReqDirection((short) 0);
                break;
            case 68:
                pacMan.setReqDirection((short) 1);
                break;
            case 83:
                pacMan.setReqDirection((short) 2);
                break;
            case 65:
                pacMan.setReqDirection((short) 3);
                break;
        }
    }
}