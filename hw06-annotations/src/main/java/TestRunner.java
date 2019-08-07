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
import java.util.Optional;

public class TestRunner {
    public void doRun(String className) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Class<?> clazz = Class.forName(className);
        List<TestRunner.TestCase> testCases = getTestCases(clazz, this);
        for (TestRunner.TestCase testCase: testCases) {
            this.runTestCase(testCase);
        }
        this.printStatistics(testCases);
    }


    private void printStatistics(List<TestCase> testCases) {
        System.out.println("\n\n\n=======\nTests executed: " + testCases.size());
    }

    private List<TestCase> getTestCases(Class<?> clazz, TestRunner runner) {
        List<Method> testMethods = runner.getTestMethods(clazz);
        Method beforeTest = runner.getAnnotatedMethods(clazz, Before.class);
        Method afterTest = runner.getAnnotatedMethods(clazz, After.class);
        List<TestCase> testCases = new ArrayList<>();

        for (Method testMethod: testMethods) {
            TestCase testCase = this.createTestCase();
            testCase.setBeforeTest(Optional.ofNullable(beforeTest));
            testCase.setAfterTest(Optional.ofNullable(afterTest));
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

        if (testCase.getBeforeTest().isPresent()) {
            testCase.getBeforeTest().get().invoke(instance);
        }

        try {
            testCase.getTestMethod().invoke(instance);
        } catch (Throwable e) {
            System.out.println("Exception thrown: " + e.toString());
            testCase.setException(e);
            testCase.setPassed(false);
        }

        if (testCase.getAfterTest().isPresent()) {
            testCase.getAfterTest().get().invoke(instance);
        }

        System.out.println("--------");
    }

    private Method getAnnotatedMethods(Class<?> clazz, Class annotationClass) {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method: declaredMethods) {
            for (Annotation annotation: method.getAnnotations()) {
                if (annotation.annotationType().equals(annotationClass)) {
                    return method;
                }
            }
        }

        return null;
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

    private TestCase createTestCase() {
        return new TestCase();
    }

    @Data
    private class TestCase {
        Class <?> clazz;
        Method testMethod;
        Optional<Method> beforeTest;
        Optional<Method> afterTest;
        Throwable exception;
        boolean isPassed;

        TestCase() {}
    }
}
