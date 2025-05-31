import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Tile {

    public Tile(JPanel panel) throws IOException {
        this.panel = panel;

        panel.setSize(40, 40);
        panel.setLayout(null);
    }


    JPanel panel;
    TileState tileState;
    JLabel label = new JLabel();



    ImageIcon emptyIcon = new ImageIcon(javax.imageio.ImageIO.read(getClass().getResourceAsStream("/Empty.png")));
    ImageIcon wallIcon = new ImageIcon(javax.imageio.ImageIO.read(getClass().getResourceAsStream("/Wall.png")));
    ImageIcon dotIcon = new ImageIcon(javax.imageio.ImageIO.read(getClass().getResourceAsStream("/Dot.png")));
    ImageIcon powerUpIcon = new ImageIcon(javax.imageio.ImageIO.read(getClass().getResourceAsStream("/PowerUp.png")));


    /**
     * Sets the tile state and updates the label accordingly.
     */
    public void setTileState(TileState tileState) {
        this.tileState = tileState;
        panel.add(label);
        label.setBounds(0, 0, 40, 40);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setVerticalAlignment(SwingConstants.TOP);
        label.setText(null);
        label.setBorder(null);


    }

    /**
     * Returns the current tile state.
     */
    public TileState getTileState() {
        updateToState();
        return tileState;
    }

    /**
     * Updates the label icon based on the current tile state.
     */
    public void updateToState() {
        switch (tileState) {
            case Empty:
               label.setIcon(emptyIcon);

                break;
            case DOT:
               label.setIcon(dotIcon);

                break;
            case WALL:
                label.setIcon(wallIcon);

                break;
            case POWER_UP:
                label.setIcon(powerUpIcon);
                break;
        }


    }

}
