package com.chokchok.chokchokapi.common.aop.aspect;

import com.chokchok.chokchokapi.common.aop.annotation.CheckRole;
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
import java.util.Arrays;
import java.util.Objects;

/**
 * AOP를 이용한 메서드 접근 시 권한(Role)을 체크하는 기능을 구현하는 클래스
 */
@Slf4j
@Aspect
@Component
public class CheckRoleAspect {
    private static final String REQUEST_ID_HEADER = "X-MEMBER-ID";
    private static final String REQUEST_ROLE_HEADER = "X-MEMBER-ROLES";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_USER = "ROLE_USER";

    // @RoleCheck 어노테이션이 붙은 메서드에만 적용
    @Pointcut("@annotation(com.chokchok.chokchokapi.common.aop.annotation.CheckRole)")
    public void roleCheckPointcut() {}

    @Before("roleCheckPointcut()")
    public void checkRole(JoinPoint joinPoint) {
        HttpServletRequest request =
                ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        String roleHeader = request.getHeader(REQUEST_ROLE_HEADER);
        String idHeader = request.getHeader(REQUEST_ID_HEADER);

        // 인증된 사용자인지 확인
        if (idHeader == null || roleHeader == null) {
            throw new AuthorizationException(ErrorCode.UNAUTHORIZED, "X-MEMBER-ID, X-MEMBER-ROLES 헤더가 없습니다.");
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CheckRole checkRole = method.getAnnotation(CheckRole.class);

        // role check
        String requiredRole = checkRole.hasRole();
        String[] anyRequiredRoles = checkRole.hasAnyRole();

        // admin은 모든 권한을 가진 사용자이므로 바로 통과
        if(roleHeader.equals(ROLE_ADMIN)) {
            return;
        }

        if(!requiredRole.isEmpty() && !roleHeader.equals(requiredRole)) {
            throw new AuthorizationException(ErrorCode.INSUFFICIENT_PERMISSION, "접근 권한이 없습니다");
        }

        if(anyRequiredRoles.length > 0 && !Arrays.asList(anyRequiredRoles).contains(roleHeader)) {
            throw new AuthorizationException(ErrorCode.INSUFFICIENT_PERMISSION, "접근 권한이 없습니다");
        }
    }

}
