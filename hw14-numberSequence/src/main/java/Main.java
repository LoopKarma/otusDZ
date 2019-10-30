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

    Map<Integer, StringBuilder> run() {
        Thread thread1 = new Thread(this::incT1);
        Thread thread2 = new Thread(this::incT2);

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

    private void incT1() {
        while (!Thread.interrupted()) {
            int i = 1;
            for (; i < 10; i++) {
                writeNumberT1(i);
            }
            for (; i > 1; i--) {
                writeNumberT1(i);
            }
            if (Thread.currentThread().isInterrupted()) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void incT2() {
        while (!Thread.interrupted()) {
            int i = 1;
            for (; i < 10; i++) {
                writeNumberT2(i);
            }
            for (; i > 1; i--) {
                writeNumberT2(i);
            }
            if (Thread.currentThread().isInterrupted()) {
                Thread.currentThread().interrupt();
            }
        }
    }


    private synchronized void writeNumberT1(int number) {
        notify();
        sequenceT1.append(number);
        try {
            wait();
            sequenceT1.append(" ");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private synchronized void writeNumberT2(int number) {
        notify();
        sequenceT2.append(" ");
        try {
            wait();
            sequenceT2.append(number);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
