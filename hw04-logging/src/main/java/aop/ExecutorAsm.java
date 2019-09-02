package aop;

import annotated.TestLoggingImpl;

public class ExecutorAsm {
    public static void main(String[] args) {
        TestLoggingImpl testLogging = new TestLoggingImpl();
        testLogging.calculation(5, 6);
        testLogging.calculation(32, 0);
        testLogging.calculation(700, -28);

        testLogging.calculationWithoutLogs(5, 6);
        testLogging.calculationWithoutLogs(32, 0);
        testLogging.calculationWithoutLogs(700, -28);
    }
}
