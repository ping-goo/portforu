package org.pinggu.portforu.common.annotation;


import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@SQLDelete(sql = "")
@Where(clause = "")
public @interface SoftDelete {
    @AliasFor(annotation = SQLDelete.class, attribute = "sql")
    String sql();

    @AliasFor(annotation = Where.class, attribute = "clause")
    String clause() default "is_deleted = false";
}
