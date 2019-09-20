package gc.bench;


import java.util.ArrayList;
import java.util.List;

class TestSubject implements TestSubjectMBean {
    private long loopCounter;
    private List<String>list = new ArrayList<>();
    private int size = 100000;

    @Override
    public void run() throws OutOfMemoryError {
        while (true) {
            for (int i = 0; i < this.size; i++) {
                list.add("random");
                loopCounter++;
            }

            list.subList(0, size/2).clear();

            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {
                System.out.println("InterruptedException ");
            }
        }
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public int getSize() {
        return this.size;
    }


    public long getLoopCounter() {
        return loopCounter;
    }
}
