package thread.sync;

public class SyncSample {
    class Printer {
        Object lock = new Object();
        public void print() throws InterruptedException {
            synchronized (lock) {
                Thread.sleep(1000);
                System.out.print("Java");
                Thread.sleep(1000);
                System.out.print(",");
                Thread.sleep(1000);
                System.out.print("hello ");
                Thread.sleep(1000);
                System.out.println("world !");
            }
        }
    }

    class PrinterTask implements Runnable {
        public Printer printer;

        @Override
        public void run() {
            try {
                printer.print();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void start() throws InterruptedException {
        Printer printer = new Printer();
        for (int i = 0; i < 10; i++) {
            PrinterTask task = new PrinterTask();
            task.printer = printer;
            Thread thread = new Thread(task);
            thread.start();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new SyncSample().start();
    }
}
