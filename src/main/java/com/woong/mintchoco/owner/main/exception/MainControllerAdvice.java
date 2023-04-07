package com.woong.mintchoco.owner.main.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class MainControllerAdvice {

    /**
     * Thymeleaf 에서 현재 페이지의 url 을 사용하기 위해 model 객체에 담아주는 메소드
     *
     * @param request request
     * @return url
     */
    @ModelAttribute("url")
    public String contextPath(HttpServletRequest request) {
        return request.getServletPath();
    }


    /**
     * Thymeleaf 에서 HttpServletRequest 를 사용하기 위해 model 객체에 담아주는 메소드
     *
     * @param request request
     * @return request
     */
    @ModelAttribute("request")
    public HttpServletRequest httpServletRequest(HttpServletRequest request) {
        return request;
    }

}
