package com.credithc.libannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author liyong
 * @date 2019/9/27 9:28
 * @description
 */
@Retention(value = RetentionPolicy.CLASS)
@Target(value = {ElementType.METHOD})
public @interface PermissionGranted {
    String[] value();
}
