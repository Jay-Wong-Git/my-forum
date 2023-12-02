package com.example.config;

import com.example.entity.RestBean;
import com.example.entity.dto.Account;
import com.example.entity.vo.response.AuthorizeVO;
import com.example.filter.JwtAuthorizeFilter;
import com.example.service.AccountService;
import com.example.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;

/**
 * Configure SpringSecurity
 *
 * @author Jay Wong
 * @date 2023/10/30
 */
@Configuration
public class SecurityConfiguration {
    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private JwtAuthorizeFilter jwtAuthorizeFilter;
    @Resource
    private AccountService accountService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(config -> config
                        .requestMatchers("/api/auth/**", "/error").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(config -> config
                        .loginProcessingUrl("/api/auth/login")
                        .successHandler(this::onAuthenticationSuccess)
                        .failureHandler(this::onAuthenticationFailure))
                .logout(config -> config
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler(this::onLogoutSuccess))
                .csrf(config -> config.disable())
                .sessionManagement(config -> config
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(config -> config
                        .authenticationEntryPoint(this::commence)
                        .accessDeniedHandler(this::handle))
                .addFilterBefore(jwtAuthorizeFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // execute after successful login
    private void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        Account account = accountService.findAccountByUsernameOrEmail(user.getUsername());
        String token = jwtUtils.createJwt(user, account.getId(), account.getUsername());
        Date expire = jwtUtils.resolveJwtFromToken(token).getExpiresAt();
        AuthorizeVO vo = account.asViewObject(AuthorizeVO.class, v -> {
            v.setToken(token);
            v.setExpire(expire);
        });
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(RestBean.success("Login successful.", vo).toJSONString());
    }

    // execute after failed login
    private void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(RestBean.failure(401, "Invalid username or password1.").toJSONString());
    }

    // execute after successful logout
    private void onLogoutSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        String headerToken = request.getHeader("Authorization");
        if (jwtUtils.invalidateJwt(headerToken)) {
            response.getWriter().write(RestBean.success("Logout successful.").toJSONString());
        } else {
            response.getWriter().write(RestBean.success("Logout failed.").toJSONString());
        }
    }

    // execute after failed authentication
    private void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(RestBean.failure(401, authException.getMessage()).toJSONString());
    }

    // execute after denied access
    private void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(RestBean.failure(401, accessDeniedException.getMessage()).toJSONString());
    }
}
