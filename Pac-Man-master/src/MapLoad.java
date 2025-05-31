import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MapLoad {


    public MapLoad() {
    filetext = getfiletext();
    }

    ArrayList<String> filetext=new ArrayList<>();


    /**
     * Reads the text file with map.
     */

    public ArrayList<String> getfiletext() {
        ArrayList<String> ret = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("MapFile"));
            String text;
            while ((text = reader.readLine()) != null) {
                ret.add(text);
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }

    /**
     *loads the map from the text file and returns the state of the tile at the given coordinates.
     */

    public TileState steteOfTile (int x, int y) {


       int data = filetext.get(y).charAt(x);


        switch (data) {
            case '0' : return TileState.DOT;
            case '1' : return TileState.WALL;
            case '2' : return TileState.Empty;
            case '3' : return TileState.POWER_UP;
        }
        return TileState.Empty;






    }


}
