package com.example.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.utils.Const;
import com.example.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author Jay Wong
 * @date 2023/10/31
 */
@Component
public class JwtAuthorizeFilter extends OncePerRequestFilter {
    @Resource
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String headerToken = request.getHeader("Authorization");
        DecodedJWT decodedJWT = jwtUtils.resolveJwtFromHeaderToken(headerToken);
        if (decodedJWT != null) {
            UserDetails userDetails = jwtUtils.toUserDetails(decodedJWT);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            request.setAttribute(Const.ATTR_USER_ID, jwtUtils.toUserId(decodedJWT));
        }
        filterChain.doFilter(request, response);
    }
}
