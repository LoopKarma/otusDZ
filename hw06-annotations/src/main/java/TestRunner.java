import annotations.After;
import annotations.Before;
import annotations.Test;
import lombok.Data;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {
    public static void main(String[] args) {
        try {
            String className = args[0];
            Class<?> clazz = Class.forName(className);
            TestRunner runner = new TestRunner();
            List<TestCase> testCases = getTestCases(clazz, runner);
            for (TestCase testCase: testCases) {
                runner.runTestCase(testCase);
            }
            runner.printStatistics(testCases);
        } catch (Exception e) {
            System.err.println("Internal Error occurred.");
            e.printStackTrace(System.err);
        }
    }

    private void printStatistics(List<TestCase> testCases) {
        System.out.println("\n\n\n=======\nTests executed: " + testCases.size());
    }

    private static List<TestCase> getTestCases(Class<?> clazz, TestRunner runner) {
        List<Method> testMethods = runner.getTestMethods(clazz);
        Method beforeTest = runner.getBeforeTestMethod(clazz);
        Method afterTest = runner.getAfterTestMethod(clazz);
        List<TestCase> testCases = new ArrayList<>();

        for (Method testMethod: testMethods) {
            TestCase testCase = runner.createTestCase();
            testCase.setBeforeTest(beforeTest);
            testCase.setAfterTest(afterTest);
            testCase.setTestMethod(testMethod);
            testCase.setClazz(clazz);
            testCases.add(testCase);
        }
        return testCases;
    }

    private void runTestCase(TestCase testCase) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        System.out.println("\n\n--------");
        Constructor<?> constructor = testCase.getClazz().getDeclaredConstructor();
        Object instance = constructor.newInstance();

        testCase.getBeforeTest().invoke(instance);
        try {
            testCase.getTestMethod().invoke(instance);
        } catch (Throwable e) {
            System.out.println("Exception thrown: " + e.toString());
            testCase.setException(e);
            testCase.setPassed(false);
        }
        testCase.getAfterTest().invoke(instance);

        System.out.println("--------");
    }

    private List<Method> getTestMethods(Class<?> clazz) {
        List<Method> testMethods = new ArrayList<Method>();
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method: declaredMethods) {
            for (Annotation annotation: method.getAnnotations()) {
                if (annotation.annotationType().equals(Test.class)) {
                    testMethods.add(method);
                }
            }
        }

        return testMethods;
    }

    private Method getBeforeTestMethod(Class<?> clazz) {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method: declaredMethods) {
            for (Annotation annotation: method.getAnnotations()) {
                if (annotation.annotationType().equals(Before.class)) {
                    return method;
                }
            }
        }

        return null;
    }

    private Method getAfterTestMethod(Class<?> clazz) {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method: declaredMethods) {
            for (Annotation annotation: method.getAnnotations()) {
                if (annotation.annotationType().equals(After.class)) {
                    return method;
                }
            }
        }

        return null;
    }

    private TestCase createTestCase() {
        return new TestCase();
    }

    @Data
    private class TestCase {
        Class <?> clazz;
        Method testMethod;
        Method beforeTest;
        Method afterTest;
        Throwable exception;
        boolean isPassed;

        TestCase() {}
    }
}
