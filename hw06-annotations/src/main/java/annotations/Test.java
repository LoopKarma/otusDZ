package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Retention(RUNTIME)
public @interface Test {
}
