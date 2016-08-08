package chankyin.mentamatics.math.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Indicates that this array parameter will be modified by the method.
 */
@Target(ElementType.PARAMETER)
public @interface Mutable{
}
