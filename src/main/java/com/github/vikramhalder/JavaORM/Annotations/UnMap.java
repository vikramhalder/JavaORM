package  com.github.vikramhalder.JavaORM.Annotations;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface UnMap {
    public String NotColoum() default "UnMap";
}
