package com.example.filter;

import com.example.utils.Const;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Jay Wong
 * @date 2023/10/31
 */
@Component
@Order(Const.CORS_ORDER) // cross-origin filter order
public class CorsFilter extends HttpFilter {
    @Override
    protected void doFilter(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        addCorsHeader(request, response);
        chain.doFilter(request, response);
    }

    private void addCorsHeader(
            HttpServletRequest request,
            HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
    }
}
