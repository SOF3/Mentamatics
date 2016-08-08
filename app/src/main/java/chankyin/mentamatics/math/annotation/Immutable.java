package chankyin.mentamatics.math.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Indicates that the value is an array whose values must not be modified.
 */
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.LOCAL_VARIABLE})
public @interface Immutable{
}
