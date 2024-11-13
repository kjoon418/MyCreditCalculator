package junwatson.mycreditcalculator.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/**
 * GenericFilterBean이나 OncePerRequestFilter를 구현하면, UsernamePasswordAuthenticationFilter 전에 동작하도록 등록된다<br>
 * GenericFilterBean은 스프링에서 제공하는 필터로, Servlet에서 제공하는 Filter를 확장하여 Spring의 설정 정보를 가져올 수 있도록 확장된 추상 클래스이다
 */
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private final TokenProvider tokenProvider;

    /**
     * UsernamePasswordAuthenticationFilter 전에 동작한다
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String token = tokenProvider.resolveToken((HttpServletRequest) request);

        // 토큰이 적합한지 확인한다
        if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
            Authentication authentication = tokenProvider.getAuthentication(token);

            // 사용자의 권한 정보를 SecurityContextHolder에 저장한다
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
