package com.fitibo.aotearoa.annotation;

import com.fitibo.aotearoa.dto.Role;

import java.lang.annotation.*;

/**
 * Created by qianhao.zhou on 8/9/16.
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Authentication {
    Role[] value() default {Role.Agent, Role.Admin};
}
