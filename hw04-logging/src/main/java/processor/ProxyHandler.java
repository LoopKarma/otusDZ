package processor;

import annotated.TestLogging;
import annotated.TestLoggingImpl;
import annotation.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ProxyHandler {
    public static TestLogging createTestLoggingProxy() {
        InvocationHandler handler = new DemoInvocationHandler(new TestLoggingImpl());
        return (TestLogging) Proxy.newProxyInstance(ProxyHandler.class.getClassLoader(),
                new Class<?>[]{TestLogging.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final TestLogging myClass;
        private Set<String> annotatedMethods;

        DemoInvocationHandler(TestLogging myClass) {
            this.myClass = myClass;
            annotatedMethods = new HashSet<>();
            collectAnnotatedMethods();
        }

        private void collectAnnotatedMethods() {
            Method[] declaredMethods = myClass.getClass().getDeclaredMethods();
            for (Method method: declaredMethods) {
                if (method.isAnnotationPresent(Log.class)) {
                    annotatedMethods.add(method.getName());
                }
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (annotatedMethods.contains(method.getName())) {
                doLogging(args);
            }

            return method.invoke(myClass, args);
        }

        private void doLogging(Object[] args) {
            String params = Arrays.stream(args)
                    .map(s -> "param: " + s.toString())
                    .collect(Collectors.joining(", "));

            System.out.println("executed method: calculation, " + params);
        }
    }
}
