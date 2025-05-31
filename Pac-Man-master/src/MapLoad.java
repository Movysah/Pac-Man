import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MapLoad {

    ArrayList<String> filetext = new ArrayList<>();

    public MapLoad() {
        filetext = getfiletext();
    }

    /**
     * Reads the text file with map using resource stream.
     */
    public ArrayList<String> getfiletext() {
        ArrayList<String> ret = new ArrayList<>();
        try (
                InputStream is = getClass().getResourceAsStream("/MapFile.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is))
        ) {
            String text;
            while ((text = reader.readLine()) != null) {
                ret.add(text);
            }
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException("Map file not found or error reading map file.", e);
        }
        return ret;
    }

    /**
     * Loads the map from the text file and returns the state of the tile at the given coordinates.
     */
    public TileState steteOfTile(int x, int y) {
        int data = 2;
        try {
            data = filetext.get(y).charAt(x);
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.toString());
            return TileState.Empty; // Return Empty if coordinates are out of bounds
        }

        switch (data) {
            case '0':
                return TileState.DOT;
            case '1':
                return TileState.WALL;
            case '2':
                return TileState.Empty;
            case '3':
                return TileState.POWER_UP;
        }
        return TileState.Empty;
    }
}