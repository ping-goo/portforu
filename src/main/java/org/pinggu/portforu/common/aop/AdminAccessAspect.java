package org.pinggu.portforu.common.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.pinggu.portforu.common.dto.AuthMember;
import org.pinggu.portforu.common.exception.CustomException;
import org.pinggu.portforu.domain.member.enums.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AdminAccessAspect {

    //TODO Admin AOP 와 Member AOP가 동일한 로직을 가지고 있습니다. 하나로 합치는게 유지보수 면에서 좋아요
    @Before("@annotation(org.pinggu.portforu.common.annotation.Admin)")
    public void adminApiAccess(JoinPoint joinPoint) {
        AuthMember authMember = (AuthMember) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserRole role = authMember.getUserRole();

        if (role != UserRole.ROLE_ADMIN) {
            throw new CustomException(HttpStatus.FORBIDDEN, "관리자 권한이 필요합니다.");
        }
    }

}
