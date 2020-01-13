package com.mt.annotation;

/**
 * @ClassName: CheckNull
 * @Description: 非空校验自定义注解
 * @Author: xiedong
 * @Date: 2020/1/13 14:02
 */
public @interface CheckNull {
   boolean check() default true; //默认校验
}
