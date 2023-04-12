package com.woong.mintchoco.common;

import com.woong.mintchoco.global.auth.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithAuthUserSecurityContextFactory implements WithSecurityContextFactory<WithAuthUser> {
    @Override
    public SecurityContext createSecurityContext(WithAuthUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        // 인증된 User 객체 생성
        User user = User.builder()
                .id(1L)
                .userId(annotation.username())
                .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
                AuthorityUtils.createAuthorityList(annotation.role()));

        context.setAuthentication(authentication);

        return context;
    }
}
