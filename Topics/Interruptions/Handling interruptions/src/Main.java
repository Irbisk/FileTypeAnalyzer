class CounterThread extends Thread {

    @Override
    public void run() {
        long counter = 0;

        while (true) {
            counter++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("It was interrupted");
                break;
            }
        }
    }
}