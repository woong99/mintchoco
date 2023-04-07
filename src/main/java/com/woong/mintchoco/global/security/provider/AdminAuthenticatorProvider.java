package com.woong.mintchoco.global.security.provider;

import com.woong.mintchoco.global.auth.entity.Authority;
import com.woong.mintchoco.global.auth.entity.User;
import com.woong.mintchoco.global.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminAuthenticatorProvider implements AuthenticationProvider {

    private final UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        User user = userService.loadUserByUsername(username).getUser();
        String dbPassword = user.getPassword();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (!passwordEncoder.matches(password, dbPassword)) {
            log.info("[관리자 페이지 - 로그인] 비밀번호가 일치하지 않습니다.");
            throw new BadCredentialsException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        if (!user.getAuthority().getAuthority().equals(Authority.ADMIN.getAuthority())) {
            log.info("[관리자 페이지 - 로그인] 권한이 없습니다. 아이디 : {}, 권한 : {}", user.getUserId(),
                    user.getAuthority().getAuthority());
            throw new DisabledException("권한이 없는 계정입니다.");
        }

        if (user.getHasPermission().equals("N")) {
            log.info("[관리자 페이지 - 로그인] 허가되지 않은 사용자입니다. 아이디 : {}, 권한 : {}", user.getUserId(),
                    user.getAuthority().getAuthority());
            throw new LockedException("허가된 사용자가 아닙니다.");
        }

        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
