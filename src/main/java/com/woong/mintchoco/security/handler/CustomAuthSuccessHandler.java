package com.woong.mintchoco.security.handler;

import com.woong.mintchoco.domain.User;
import com.woong.mintchoco.repository.user.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String requestLoginType = request.getRequestURI().split("/")[1];
        User user = userRepository.findByUserId(authentication.getName());
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        log.info("[로그인] - 아이디 : {}, 권한 : {}, 로그인 url : {}", authentication.getName(), roles, requestLoginType);

        String redirectUrl = switch (requestLoginType) {
            case "admin" -> "/admin/main";
            case "owner" -> "/owner/main";
            case "user" -> "/user/main";
            default -> "error";
        };

        response.sendRedirect(redirectUrl);
    }
}
