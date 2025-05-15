import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Frame implements KeyListener {


    JFrame frame;
    private Tile[][] tiles;//[x][y]
    JPanel mapPanel = new JPanel();
    JPanel pacManPanel = new JPanel();
    JLabel pacManLabel = new JLabel();
    PacMan pacMan = new PacMan();

    public Frame() {
        MapLoad mapLoad = new MapLoad();


        pacManLabel.setIcon(new ImageIcon("Pac-Man.png"));
        pacManLabel.setBorder(new LineBorder(Color.red));
        pacManLabel.setBounds(0, 0, 40, 40);
        pacManLabel.setBackground(new Color(0, 0, 0));
        pacManLabel.setOpaque(false);


        pacManPanel.setLayout(null);
        pacManPanel.setBounds(0, 0, 40, 40);
        pacManPanel.add(pacManLabel);
        pacManPanel.setOpaque(false);


        frame = new JFrame();
        frame.setSize(19 * 40 + 16, 21 * 40 + 39);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.addKeyListener(this);

        mapPanel.setSize(frame.getWidth(), frame.getHeight());
        mapPanel.setBounds(0, 0, 19 * 40, 21 * 40);
        mapPanel.setLayout(new GridLayout(21, 19));


        tiles = new Tile[19][21];


        for (int i = 0; i < 19 * 21; i++) {
            JPanel panel = new JPanel();


            tiles[(i % 19)][i / 19] = new Tile(panel);
            tiles[(i % 19)][(i / 19)].setTileState(mapLoad.steteOfTile(i % 19, i / 19));
            tiles[(i % 19)][i / 19].updateToState();
            mapPanel.add(panel);


        }
        JLayeredPane layeredPane = new JLayeredPane();

        layeredPane.add(mapPanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(pacManPanel, JLayeredPane.PALETTE_LAYER);

        frame.setLayeredPane(layeredPane);

        frame.setVisible(true);
    }


    public void setpacManPosition(double x, double y) {


        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 21; j++) {
                tiles[i][j].label.setBorder(new LineBorder(Color.black));
            }
        }
        tiles[pacMan.getXloc()][pacMan.getYloc()].label.setBorder(new LineBorder(Color.green));

        if (x == -0.9) {
    x=18;
        } else if (x == 18) {
            x= -0.9;
        }


        pacManPanel.setLocation((int) (40 * x), (int) (40 * y));

        pacMan.setxPosition(x);
        pacMan.setyPosition(y);

    }


    public void eat(){
        if(tiles[pacMan.getXloc()][pacMan.getYloc()].getTileState().equals(TileState.Dot)
        ||(tiles[pacMan.getXloc()][pacMan.getYloc()].getTileState().equals(TileState.PowerUp))) {
            tiles[pacMan.getXloc()][pacMan.getYloc()].setTileState(TileState.Empty);
        }
    }


    public TileState getStateOnPosition(double xpos, double ypos) {

        return tiles[(int) xpos][(int) ypos].getTileState();
    }


    public void movePacMan() {

        pacMan.updateLocation();

        eat();


        switch (pacMan.getReqDirection()) {
            case 0:
                if (!getStateOnPosition(pacMan.getxPosition(), pacMan.getyPosition() - 0.11).equals(TileState.Wall)
                        && (!getStateOnPosition(pacMan.getxPosition() + 0.99, pacMan.getyPosition() - 0.11).equals(TileState.Wall))) {
                    setpacManPosition(pacMan.getxPosition(), pacMan.getyPosition() - 0.01);
                    pacMan.updateDirection();
                    return;
                }
                break;
            case 1:
                if (!getStateOnPosition(pacMan.getxPosition() + 1.0, pacMan.getyPosition()).equals(TileState.Wall)
                        && (!getStateOnPosition(pacMan.getxPosition() + 1.0, pacMan.getyPosition() + 0.99).equals(TileState.Wall))) {
                    setpacManPosition(pacMan.getxPosition() + 0.01, pacMan.getyPosition());
                    pacMan.updateDirection();
                    return;
                }
                break;
            case 2:
                if (!getStateOnPosition(pacMan.getxPosition(), pacMan.getyPosition() + 1.0).equals(TileState.Wall)
                        && (!getStateOnPosition(pacMan.getxPosition() + 0.99, pacMan.getyPosition() + 1.0).equals(TileState.Wall))) {
                    setpacManPosition(pacMan.getxPosition(), pacMan.getyPosition() + 0.01);
                    pacMan.updateDirection();
                    return;
                }
                break;
            case 3:
                if (!getStateOnPosition(pacMan.getxPosition() - 0.01, pacMan.getyPosition()).equals(TileState.Wall)
                        && (!getStateOnPosition(pacMan.getxPosition() - 0.01, pacMan.getyPosition() + 0.99).equals(TileState.Wall))) {
                    setpacManPosition(pacMan.getxPosition() - 0.01, pacMan.getyPosition());
                    pacMan.updateDirection();
                    return;
                }
                break;
        }
        switch (pacMan.getDirection()) {
            case 0:
                if (!getStateOnPosition(pacMan.getxPosition(), pacMan.getyPosition() - 0.01).equals(TileState.Wall)
                        && (!getStateOnPosition(pacMan.getxPosition() + 0.99, pacMan.getyPosition() - 0.01).equals(TileState.Wall))) {
                    setpacManPosition(pacMan.getxPosition(), pacMan.getyPosition() - 0.01);
                    return;
                }
                break;
            case 1:
                if (!getStateOnPosition(pacMan.getxPosition() + 1.0, pacMan.getyPosition()).equals(TileState.Wall)
                        && (!getStateOnPosition(pacMan.getxPosition() + 1.0, pacMan.getyPosition() + 0.99).equals(TileState.Wall))) {
                    setpacManPosition(pacMan.getxPosition() + 0.01, pacMan.getyPosition());
                    return;
                }
                break;
            case 2:
                if (!getStateOnPosition(pacMan.getxPosition(), pacMan.getyPosition() + 1.0).equals(TileState.Wall)
                        && (!getStateOnPosition(pacMan.getxPosition() + 0.99, pacMan.getyPosition() + 1.0).equals(TileState.Wall))) {
                    setpacManPosition(pacMan.getxPosition(), pacMan.getyPosition() + 0.01);
                    return;
                }
                break;
            case 3:
                if (!getStateOnPosition(pacMan.getxPosition() - 0.01, pacMan.getyPosition()).equals(TileState.Wall)
                        && (!getStateOnPosition(pacMan.getxPosition() - 0.01, pacMan.getyPosition() + 0.99).equals(TileState.Wall))) {
                    setpacManPosition(pacMan.getxPosition() - 0.01, pacMan.getyPosition());
                    return;
                }
                break;
        }


    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {


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

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
