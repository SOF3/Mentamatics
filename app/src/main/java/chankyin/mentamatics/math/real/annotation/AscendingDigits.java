package chankyin.mentamatics.math.real.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Indicates that the value (or return value of the method) is an array starting from the least significant digit.
 */
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD, ElementType.LOCAL_VARIABLE})
public @interface AscendingDigits{
}
