public class Main {
    public static void main(String[] args) {

        Frame frame = new Frame();

      frame.setpacManPosition(9, 15);


        do {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            frame.movePacMan();
        } while (true);


    }
}