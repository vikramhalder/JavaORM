package  com.github.vikramhalder.JavaORM.Annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface AutoIncrement {
    public String AutoIncrement() default "AutoIncrement";
}
