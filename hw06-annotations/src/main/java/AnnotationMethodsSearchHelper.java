import annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AnnotationMethodsSearchHelper {
    private static List<Method> getAnnotatedMethods(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        List<Method> annotationMethods = new ArrayList<>();
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method: declaredMethods) {
            if (method.isAnnotationPresent(annotationClass)) {
                annotationMethods.add(method);
            }
        }

        return annotationMethods;
    }

    public static Method getAnnotatedMethod(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        return getAnnotatedMethods(clazz, annotationClass).stream().findFirst().orElse(null);
    }

    public static List<Method> getTestMethods(Class<?> clazz) {
        return getAnnotatedMethods(clazz, Test.class);
    }
}
