package io.qhzhou.nztrip.annotation;

import java.lang.annotation.*;

/**
 * Created by qianhao.zhou on 8/9/16.
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthenticationPass {
    boolean validate() default true;
}
