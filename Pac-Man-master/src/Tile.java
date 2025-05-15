import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class Tile {

    public Tile(JPanel panel) {
        this.panel = panel;

        panel.setSize(40, 40);
        panel.setLayout(null);






    }


    JPanel panel;
    TileState tileState;
    JLabel label = new JLabel();

    ImageIcon emptyIcon = new ImageIcon("Empty.png");
    ImageIcon wallIcon = new ImageIcon("Wall.png");
    ImageIcon dotIcon = new ImageIcon("Dot.png");
    ImageIcon powerUpIcon = new ImageIcon("PowerUp.png");




    public void setTileState(TileState tileState) {
        this.tileState = tileState;
        panel.add(label);
        label.setBounds(0, 0, 40, 40);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setVerticalAlignment(SwingConstants.TOP);
        label.setText(null);
        label.setBorder(null);


    }

    public TileState getTileState() {
        updateToState();
        return tileState;
    }


    public void updateToState() {
        switch (tileState) {
            case Empty:
               label.setIcon(emptyIcon);
                // panel.setBackground(Color.black);
                break;
            case Dot:
               label.setIcon(dotIcon);
                // panel.setBackground(Color.darkGray);
                break;
            case Wall:
                label.setIcon(wallIcon);
                // panel.setBackground(Color.blue);
                break;
            case PowerUp:
                label.setIcon(powerUpIcon);
                // panel.setBackground(Color.orange);
                break;
        }


    }

}
