package org.checkpoint.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.checkpoint.dominio.autenticacao.Autenticacao;
import org.checkpoint.dominio.user.User;

public class AuthUtil {
    private final Autenticacao autenticacao;

    public AuthUtil(Autenticacao autenticacao) {
        this.autenticacao = autenticacao;
    }

    private String extrairTokenDoCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if ("token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public User getAuth(HttpServletRequest request) {
        try {
            return autenticacao.verificarToken(extrairTokenDoCookie(request));
        } catch (Exception e) {
            return null;
        }
    }
}
