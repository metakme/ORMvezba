package Db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* Use to annotate each field that 
* corresponds to a column in database
* <p> Set column name if it differs from
* the field name. Otherwise you can
* leave default.
* 
* @author kme
*/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String column() default ""; 
}
