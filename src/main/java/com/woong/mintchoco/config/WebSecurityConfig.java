package com.woong.mintchoco.config;

import com.woong.mintchoco.domain.Authority;
import com.woong.mintchoco.security.handler.CustomAuthFailureHandler;
import com.woong.mintchoco.security.handler.CustomAuthSuccessHandler;
import com.woong.mintchoco.security.provider.AdminAuthenticatorProvider;
import com.woong.mintchoco.security.provider.OwnerAuthenticatorProvider;
import com.woong.mintchoco.security.provider.UserAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final AdminAuthenticatorProvider adminAuthenticatorProvider;
    private final OwnerAuthenticatorProvider ownerAuthenticatorProvider;
    private final UserAuthenticationProvider userAuthenticationProvider;
    private final CustomAuthSuccessHandler customAuthSuccessHandler;
    private final CustomAuthFailureHandler customAuthFailureHandler;
    private static final String usernameParameter = "userId";
    private static final String passwordParameter = "userPwd";

    private static final String[] SECURITY_EXCLUDE_PATTERN = {
            "/bootstrap/**",
            "/css/**",
            "/js/**",
            "/fontawesome-free/**",
            "/img/**",
            "/*/login",
            "/*/signUp",
            "/*/signUp.do",
            "/*/duplicateCheck.do"
    };

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(adminAuthenticatorProvider, ownerAuthenticatorProvider, userAuthenticationProvider);
    }

    @Order(1)
    @Bean
    public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/admin/**")
                .authorizeHttpRequests(
                        auth -> auth.requestMatchers(SECURITY_EXCLUDE_PATTERN)
                                .permitAll())
                .authorizeHttpRequests(
                        auth -> auth.requestMatchers("/admin/**").hasAuthority(Authority.ADMIN.getAuthority())
                )
                .formLogin()
                .loginPage("/admin/login")
                .loginProcessingUrl("/admin/login.do")
                .usernameParameter(usernameParameter)
                .passwordParameter(passwordParameter)
                .successHandler(customAuthSuccessHandler)
                .failureHandler(customAuthFailureHandler)
                .and()
                .authenticationProvider(adminAuthenticatorProvider)
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Order(2)
    @Bean
    public SecurityFilterChain ownerFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/owner/**")
                .authorizeHttpRequests(
                        auth -> auth.requestMatchers(SECURITY_EXCLUDE_PATTERN)
                                .permitAll())
                .authorizeHttpRequests(
                        auth -> auth.requestMatchers("/owner/**").hasAuthority(Authority.OWNER.getAuthority())
                )
                .formLogin()
                .loginPage("/owner/login")
                .loginProcessingUrl("/owner/login.do")
                .usernameParameter(usernameParameter)
                .passwordParameter(passwordParameter)
                .successHandler(customAuthSuccessHandler)
                .failureHandler(customAuthFailureHandler)
                .and()
                .authenticationProvider(ownerAuthenticatorProvider)
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Order(3)
    @Bean
    public SecurityFilterChain userFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/user/**")
                .authorizeHttpRequests(
                        auth -> auth.requestMatchers(SECURITY_EXCLUDE_PATTERN)
                                .permitAll())
                .authorizeHttpRequests(
                        auth -> auth.requestMatchers("/user/**").hasAuthority(Authority.USER.getAuthority())
                )
                .formLogin()
                .loginPage("/user/login")
                .loginProcessingUrl("/user/login.do")
                .usernameParameter(usernameParameter)
                .passwordParameter(passwordParameter)
                .successHandler(customAuthSuccessHandler)
                .failureHandler(customAuthFailureHandler)
                .and()
                .authenticationProvider(userAuthenticationProvider)
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }
}
