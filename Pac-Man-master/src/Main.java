public class Main {
    public static void main(String[] args) {

        Frame frame = new Frame();

        frame.setpacManPosition(9, 15);

        for (int i = 0; i < 100000; i++) {


            //do {
            long frameStart = System.currentTimeMillis();
            frame.movePacMan();
            frame.moveGhosts();

            try {
                Thread.sleep(Math.max(0, 100 - (System.currentTimeMillis() - frameStart)));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // } while (true);

        }
    }
}