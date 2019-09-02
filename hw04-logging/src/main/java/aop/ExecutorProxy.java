package aop;

import annotated.TestLogging;
import processor.ProxyHandler;

public class ExecutorProxy {
    public static void main(String[] args) {
        executeProxyCalls();
    }

    private static void executeProxyCalls() {
        TestLogging testLogging = ProxyHandler.createTestLoggingProxy();
        testLogging.calculation(5, 6);
        testLogging.calculation(32, 0);
        testLogging.calculation(700, -28);

        testLogging.calculationWithoutLogs(5, 6);
        testLogging.calculationWithoutLogs(32, 0);
        testLogging.calculationWithoutLogs(700, -28);
    }
}