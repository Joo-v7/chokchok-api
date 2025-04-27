package com.chokchok.chokchokapi.common.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 권한(role) 체크를 위한 어노테이션 설정 클래스
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckRole {
    /**
     * 정확한 role 을 요구할 때 사용
     */
    String hasRole() default "";

    /**
     * 여러 역할 중 하나라도 일치하면 접근을 허용할 때 사용
     */
    String[] hasAnyRole() default {};
}
