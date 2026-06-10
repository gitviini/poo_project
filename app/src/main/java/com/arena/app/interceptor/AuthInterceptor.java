package com.arena.app.interceptor;

import com.arena.app.model.User;
import com.arena.app.repository.UserRepository;
import com.arena.app.service.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.Optional;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();

        // Skip auth for static resources and public endpoints
        if (path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/img/") || 
            path.equals("/login") || path.equals("/signup") || path.equals("/error")) {
            return true;
        }

        Cookie[] cookies = request.getCookies();
        String token = null;

        if (cookies != null) {
            token = Arrays.stream(cookies)
                    .filter(c -> "auth_token".equals(c.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }

        if (token != null) {
            try {
                String userId = tokenService.extractUserId(token);
                String role = tokenService.extractRole(token);

                Optional<User> userOpt = userRepository.findByUserId(userId);
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    request.setAttribute("authenticatedUser", user);

                    // RBAC check for /admin/**
                    if (path.startsWith("/admin") && !"admin".equals(role)) {
                        response.sendRedirect("/");
                        return false;
                    }

                    return true;
                }
            } catch (Exception e) {
                // Invalid token, clear cookie and redirect to login
                clearAuthCookie(response);
            }
        }

        response.sendRedirect("/login");
        return false;
    }

    private void clearAuthCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("auth_token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
