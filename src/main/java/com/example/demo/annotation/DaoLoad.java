package com.example.demo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *DAO注解
 * @author jiejT
 * @date 2021/11/16
 * @param	null
 * @return
 */
@Retention(RetentionPolicy.RUNTIME) //在运行时可以获取
@Target({ ElementType.TYPE}) //作用到类，方法，接口上等
public @interface DaoLoad {
    public String value() default "";
}
