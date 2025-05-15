public class Main {
    public static void main(String[] args) {

        Frame frame = new Frame();

      frame.setpacManPosition(9, 15);


        do {
            long frameStart = System.currentTimeMillis();
            frame.movePacMan();

            try {
                Thread.sleep(5-(frameStart-System.currentTimeMillis()));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        } while (true);


    }
}