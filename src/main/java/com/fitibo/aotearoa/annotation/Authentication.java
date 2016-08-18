package com.fitibo.aotearoa.annotation;

import com.fitibo.aotearoa.dto.Role;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
