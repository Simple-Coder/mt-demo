package com.mt.annotation;

import java.lang.annotation.*;

/**
 * @ClassName: AESAndSign
 * @Description: 加解密验签注解
 * @Author: xiedong
 * @Date: 2020/1/13 14:01
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AESAndSign {
    boolean encode() default true;//默认加密
    boolean decode() default true;//默认解密
    boolean addSign() default true;//默认加签
    boolean checkSign() default true;//默认验签
}