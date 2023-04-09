package com.woong.mintchoco.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.woong.mintchoco.global.auth.entity.Authority;
import com.woong.mintchoco.global.auth.entity.User;
import com.woong.mintchoco.global.auth.model.UserVO;
import com.woong.mintchoco.global.auth.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OwnerController.class)
class OwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("사장님 페이지 - 아이디 중복 검사")
    void 아이디_중복_검사() throws Exception {
        // given
        String id = "testId";
        given(userService.duplicateIdCheck(id)).willReturn(true);

        // when
        mockMvc.perform(
                        post("/owner/duplicateCheck.do")
                                .param("id", id)
                                .with(csrf()))
                .andExpect(handler().handlerType(OwnerController.class))
                .andExpect(handler().methodName("duplicateCheck"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("사장님 페이지 - 회원가입 성공")
    void 회원가입_성공() throws Exception {
        // given
        given(userService.signUp(any(UserVO.class))).willReturn(new User());

        // when
        mockMvc.perform(
                        post("/owner/signUp.do")
                                .param("userId", "testId")
                                .param("userPwd", "testPwd")
                                .param("name", "testName")
                                .param("tel", "testTel")
                                .with(csrf()))
                .andExpect(handler().handlerType(OwnerController.class))
                .andExpect(handler().methodName("signUpAction"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "회원가입에 성공했습니다.\n관리자의 승인이 이루어진 이후부터 로그인이 가능합니다."))
                .andExpect(model().attribute("returnUrl", "/owner/login"))
                .andExpect(view().name("/views/common/message"));
    }

    @Test
    @DisplayName("사장님 페이지 - 회원가입 실패")
    void 회원가입_실패() throws Exception {
        // given
        given(userService.signUp(any(UserVO.class))).willReturn(null);

        // when
        mockMvc.perform(
                        post("/owner/signUp.do")
                                .param("userId", "testId")
                                .param("userPwd", "testPwd")
                                .param("name", "testName")
                                .param("tel", "testTel")
                                .with(csrf()))
                .andExpect(handler().handlerType(OwnerController.class))
                .andExpect(handler().methodName("signUpAction"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "회원가입에 실패했습니다."))
                .andExpect(model().attribute("returnUrl", "/owner/sign-up"))
                .andExpect(view().name("/views/common/message"));
    }

    @Test
    @DisplayName("사장님 페이지 - 비로그인 상태에서 로그인 페이지로 이동")
    void 비로그인_로그인페이지() throws Exception {
        // when
        mockMvc.perform(
                        post("/owner/login")
                                .with(csrf())
                )
                .andExpect(handler().handlerType(OwnerController.class))
                .andExpect(handler().methodName("login"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @DisplayName("사장님 페이지 - 비로그인 상태에서 오류 발생 시")
    void 비로그인_오류() throws Exception {
        mockMvc.perform(
                        post("/owner/login")
                                .param("error", "true")
                                .param("message", "오류메시지")
                                .with(csrf())
                )
                .andExpect(handler().handlerType(OwnerController.class))
                .andExpect(handler().methodName("login"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "오류메시지"))
                .andExpect(model().attribute("returnUrl", "/owner/login"));
    }

    @Test
    @DisplayName("사장님 페이지 - 로그인 상태에서 로그인 페이지로 이동 시 메인으로 이동")
    @WithMockUser
    void 로그인후_로그인_페이지() throws Exception {
        mockMvc.perform(
                        post("/owner/login")
                                .with(csrf())
                )
                .andExpect(handler().handlerType(OwnerController.class))
                .andExpect(handler().methodName("login"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/owner/main"));
    }

    @TestConfiguration
    static class WebSecurityConfig {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            return http
                    .authorizeHttpRequests(
                            auth -> auth.requestMatchers("/owner/login", "/owner/signUp", "/owner/signUp.do",
                                            "/owner/duplicateCheck.do",
                                            "/bootstrap/**", "/css/**", "/js/**")
                                    .permitAll())
                    .authorizeHttpRequests(
                            auth -> auth.requestMatchers("/owner/**").hasAuthority(Authority.OWNER.getAuthority())
                    )
                    .formLogin()
                    .loginPage("/owner/login")
                    .loginProcessingUrl("/owner/login.do")
                    .usernameParameter("userId")
                    .passwordParameter("userPwd")
                    .and()
                    .csrf(AbstractHttpConfigurer::disable)
                    .build();
        }
    }
}