import annotations.After;
import annotations.Before;
import exception.TestCaseException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class TestRunner {
    void doRun(String className) throws TestCaseException, ClassNotFoundException {
        Class<?> clazz = Class.forName(className);
        List<TestRunner.TestCase> testCases = getTestCases(clazz);
        for (TestRunner.TestCase testCase: testCases) {
            try {
                this.runTestCase(testCase);
            } catch (ReflectiveOperationException e) {
                throw new TestCaseException(e);
            }
        }
        this.printStatistics(testCases);
    }


    private void printStatistics(List<TestCase> testCases) {
        System.out.println("\n\n\n=======\nTests executed: " + testCases.size());
    }

    private List<TestCase> getTestCases(Class<?> clazz) {
        List<Method> testMethods = AnnotationMethodsSearchHelper.getTestMethods(clazz);
        Method beforeTest = AnnotationMethodsSearchHelper.getAnnotatedMethod(clazz, Before.class);
        Method afterTest = AnnotationMethodsSearchHelper.getAnnotatedMethod(clazz, After.class);

        return testMethods
                .stream()
                .map(m -> new TestCase(clazz, m, beforeTest, afterTest, null, false))
                .collect(Collectors.toList());
    }

    private void runTestCase(TestCase testCase) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        System.out.println("\n\n--------");
        Constructor<?> constructor = testCase.getClazz().getDeclaredConstructor();
        Object instance = constructor.newInstance();

        testCase.getBeforeTest().ifPresent(method -> invoke(method, instance));
        try {
            testCase.getTestMethod().invoke(instance);
        } catch (Throwable e) {

            System.out.println("Exception thrown during running test: " + e.getCause().toString());
            testCase.setException(e.getCause());
            testCase.setPassed(false);
        }
        testCase.getAfterTest().ifPresent(method -> invoke(method, instance));

        System.out.println("--------");
    }

    @SneakyThrows
    private void invoke(Method method, Object instance) {
        method.invoke(instance);
    }

    @Data
    @AllArgsConstructor
    private class TestCase {
        Class <?> clazz;
        Method testMethod;
        Method beforeTest;
        Method afterTest;
        Throwable exception;
        boolean isPassed;

        TestCase() {}

        Optional<Method> getBeforeTest() {
            return Optional.ofNullable(beforeTest);
        }

        Optional<Method> getAfterTest() {
            return Optional.ofNullable(afterTest);
        }
    }
}
