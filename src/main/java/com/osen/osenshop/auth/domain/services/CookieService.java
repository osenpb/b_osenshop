package com.osen.osenshop.auth.domain.services;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CookieService {

    @Value("${application.security.jwt.expiration}")
    private int accessTokenExpiration;

    @Value("${application.security.jwt.refresh-expiration}")
    private int refreshTokenExpiration;

    @Value("${application.security.cookies.secure:false}")
    private boolean isSecure;

    @Value("${application.security.cookies.same-site:Lax}")
    private String sameSite;

    public void addTokenCookies(HttpServletResponse response, Map<String, String> tokens) {
        addCookie(response, "access_token", tokens.get("accessToken"), accessTokenExpiration);
        addCookie(response, "refresh_token", tokens.get("refreshToken"), refreshTokenExpiration);
    }

    private void addCookie(HttpServletResponse response, String name, String value, int expirationMinutes) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(isSecure)
                .path("/")
                .maxAge(expirationMinutes * 60)
                .sameSite(sameSite) // "Lax" es ideal para desarrollo
                .build();

        // addHeader para que NO se sobreescriban entre ellas
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void clearTokenCookies(HttpServletResponse response) {
        clearCookie(response, "access_token");
        clearCookie(response, "refresh_token");
    }

    private void clearCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}