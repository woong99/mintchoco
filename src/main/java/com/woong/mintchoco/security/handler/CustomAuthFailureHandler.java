package com.woong.mintchoco.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class CustomAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String requestLoginType = request.getRequestURI().split("/")[1];
        String errorMessage;
        if (exception instanceof BadCredentialsException) {
            errorMessage = "아이디 또는 비밀번호가 일치하지 않습니다.";
        } else if (exception instanceof UsernameNotFoundException) {
            errorMessage = "존재하지 않는 계정입니다.";
        } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            errorMessage = "허가되지 않은 계정입니다. 관리자에게 문의해주세요.";
        } else if (exception instanceof DisabledException) {
            errorMessage = "권한이 없는 계정입니다.";
        } else {
            errorMessage = "알 수 없는 오류로 인해 로그인 요청을 처리할 수 없습니다. 관리자에게 문의해주세요.";
        }

        errorMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
        String defaultFailureUrl = switch (requestLoginType) {
            case "admin" -> "/admin/login?error=true&message=";
            case "owner" -> "/owner/login?error=true&message=";
            case "user" -> "/user/login?error=true&message=";
            default -> "/error";
        };
        setDefaultFailureUrl(defaultFailureUrl + errorMessage);
        super.onAuthenticationFailure(request, response, exception);
    }
}
