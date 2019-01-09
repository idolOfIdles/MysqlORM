package safayat.orm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by safayat on 10/25/18.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD) //can use in method only.
public @interface ManyToMany {
    String nativeColumnName() default "";
    String nativeRelationColumnName() default "";
    String matchingColumnName() default "";
    String matchingRelationColumnName() default "";
    String relationTable() default "";
    String name();
    Class type();
}
