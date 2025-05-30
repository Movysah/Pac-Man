import java.io.IOException;

public class Main {
    public static void main(String[] args) {


        Frame frame = null;
        try {
            frame = new Frame();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        frame.setpacManPosition(9, 15);

        for (int i = 0; i < 100000; i++) {
            long frameStart = System.currentTimeMillis();
            frame.movePacMan();
            frame.moveGhosts();

            try {
                Thread.sleep(Math.max(0, 100 - (System.currentTimeMillis() - frameStart)));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}