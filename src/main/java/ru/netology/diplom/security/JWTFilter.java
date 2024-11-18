package ru.netology.diplom.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import ru.netology.diplom.config.AuthenticationConfigConstants;
import java.io.IOException;
@Component
@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends GenericFilterBean {

    private final JWTToken jwtToken;

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AuthenticationConfigConstants.AUTH_TOKEN);
        if (StringUtils.hasText(bearer) && bearer.startsWith(AuthenticationConfigConstants.TOKEN_PREFIX)) {
            return bearer.substring(7);
        }
        return null;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token = getTokenFromRequest((HttpServletRequest) servletRequest);
        if (token != null && jwtToken.validateAccessToken(token)) {
            log.info("Токен активен: {}", token);
            Claims claims = jwtToken.getAccessClaims(token);

            JWTAuthentication jwtAuthentication = new JWTAuthentication();
            jwtAuthentication.setUsername(claims.getSubject());
            jwtAuthentication.setAuthenticated(true);

            SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }


}
