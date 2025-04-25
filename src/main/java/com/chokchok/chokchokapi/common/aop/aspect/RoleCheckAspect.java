package com.chokchok.chokchokapi.common.aop.aspect;

import com.chokchok.chokchokapi.common.aop.annotation.RoleCheck;
import com.chokchok.chokchokapi.common.exception.base.AuthorizationException;
import com.chokchok.chokchokapi.common.exception.code.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * AOP를 이용한 메서드 접근 시 권한(Role)을 체크하는 기능을 구현하는 클래스
 */
@Slf4j
@Aspect
@Component
public class RoleCheckAspect {
    private static final String REQUEST_ROLE_HEADER = "X-MEMBER-ROLE";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    // @RoleCheck 어노테이션이 붙은 메서드에만 적용
    @Pointcut("@annotation(com.chokchok.chokchokapi.common.aop.annotation.RoleCheck)")
    public void roleCheckPointcut() {}

    @Before("roleCheckPointcut()")
    public void checkRole(JoinPoint joinPoint) {
        HttpServletRequest request =
                ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        String roleHeader = request.getHeader(REQUEST_ROLE_HEADER);

        // 인증된 사용자인지 확인
        if (roleHeader == null) {
            throw new AuthorizationException(ErrorCode.UNAUTHORIZED, "인증되지 않은 사용자 입니다.");
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RoleCheck roleCheck = method.getAnnotation(RoleCheck.class);
        String requiredRole = roleCheck.role();

        // admin은 모든 권한을 가진 사용자
        if(roleHeader.equals(ROLE_ADMIN)) {
            return;
        }

        // 권한 비교
        if (!roleHeader.equals(requiredRole)) {
            throw new AuthorizationException(ErrorCode.INSUFFICIENT_PERMISSION, "접근 권한이 없습니다.");
        }
    }
}
