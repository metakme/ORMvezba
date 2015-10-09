package Db;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Use this to Annotate the class that will
 * be mapped to the Database column
 * <p> tableName value must be set to be 
 * the same as the name of the Table in database
 * and the class needs to have a no argument 
 * Constructor
 * <p> mark each field that maps to a database
 * column with {@link Db.Column} Annotation and mark the
 * id field with the {@link Db.Id} annotation as well
 * <p> fields must be declared in the same order as columns
 * in database
 * @author kme
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AREntity {
    /**
    * tableName must be set to the same value
    * as the name of the Table in database
     * @return 
     * name of the table
    */
    String tableName();
}
