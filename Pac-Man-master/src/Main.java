public class Main {
    public static void main(String[] args) {

        Frame frame = new Frame();

      frame.setpacManPosition(9, 15);


        do {
            long frameStart = System.currentTimeMillis();
            frame.movePacMan();
            frame.moveGhosts();

            try {
                Thread.sleep(Math.max(0, 50 - (System.currentTimeMillis() - frameStart)));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        } while (true);


    }
}