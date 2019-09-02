package annotated;

import annotation.Log;

public class TestLoggingImpl implements TestLogging {
    @Log
    public void calculation(int a, int b) {
        System.out.println("execute calculation: with logs");
    }

    public void calculationWithoutLogs(int a, int b) {
        System.out.println("execute calculation: no logs");
    }
}
