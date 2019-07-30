package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
public @interface ExpectedException {
    Class<?> value();
}
