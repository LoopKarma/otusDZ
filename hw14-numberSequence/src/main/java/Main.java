import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        var main = new Main();
        Map<Integer, StringBuilder> result = main.run();
        System.out.println(result.get(1).toString());
        System.out.println(result.get(2).toString());
    }

    private StringBuilder sequenceT1 = new StringBuilder("");
    private StringBuilder sequenceT2 = new StringBuilder("");
    private String lastThreadExecuted;

    Map<Integer, StringBuilder> run() {
        Thread thread1 = new Thread(new ThreadTask(sequenceT1));
        Thread thread2 = new Thread(new ThreadTask(sequenceT2));
        thread1.setName("1");
        thread2.setName("2");

        thread1.start();
        thread2.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            return Collections.emptyMap();
        }
        thread1.interrupt();
        thread2.interrupt();

        HashMap<Integer, StringBuilder> result = new HashMap<>();
        result.put(1, sequenceT1);
        result.put(2, sequenceT2);

        return result;
    }

    class ThreadTask implements Runnable {
        StringBuilder sequence;

        ThreadTask(StringBuilder sequence) {
            this.sequence = sequence;
        }

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                int i = 1;
                for (; i < 10; i++) {
                    writeNumber(sequence, i);
                }
                if (Thread.currentThread().getName().equals("2")) {
                    try {
                        System.out.println(Thread.currentThread().getName() + " - sleeping");
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                for (; i > 1; i--) {
                    writeNumber(sequence, i);
                }
            }
        }
    }


    private synchronized void writeNumber(StringBuilder sequence, int number) {
        String threadName = Thread.currentThread().getName();
        if (lastThreadExecuted != null && lastThreadExecuted.equals(threadName)) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        if (lastThreadExecuted == null) {
            System.out.println(number + "(" + Thread.currentThread().getName() + ")");
//            sequence.append(number + "(" + Thread.currentThread().getName() + ")");
            lastThreadExecuted = threadName;
        } else if (!lastThreadExecuted.equals(threadName))  {
            System.out.println(" " + "(" + Thread.currentThread().getName() + ")");
            System.out.println(number + "(" + Thread.currentThread().getName() + ")");
//            sequence.append(" " + "(" + Thread.currentThread().getName() + ")");
//            sequence.append(number + "(" + Thread.currentThread().getName() + ")");
            lastThreadExecuted = threadName;
        }
        notify();
    }
}
