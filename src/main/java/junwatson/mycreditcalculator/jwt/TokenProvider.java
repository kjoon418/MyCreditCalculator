package junwatson.mycreditcalculator.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import junwatson.mycreditcalculator.domain.Member;
import junwatson.mycreditcalculator.exception.token.IllegalTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Component
public class TokenProvider {

    private static final String ROLE_CLAIM = "ROLE";
    private static final String BEARER = "Bearer ";
    private static final String AUTHORIZATION = "Authorization";

    private final Key key;
    private final long accessTokenValidityTime;

    public TokenProvider(@Value("${jwt.secret}") String secretKey,
                         @Value("${jwt.access-token-validity-in-milliseconds}") long accessTokenValidityTime) {
        // secretKey를 이용해서 keyBytes를 생성한다
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        // key byte array를 기반으로, 적절한 HMAC 알고리즘을 적용한 Key 객체를 생성한다
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenValidityTime = accessTokenValidityTime;
    }

    public String createAccessToken(Member member) {
        long nowTime = new Date().getTime();
        Date accessTokenExpiredTime = new Date(nowTime + accessTokenValidityTime);

        return Jwts.builder()
                // 토큰 제목(sub 클레임)을 지정한다
                .setSubject(member.getId().toString())
                // 클레임을 추가함 - "Role":"멤버의 ID"
                .claim(ROLE_CLAIM, member.getRole())
                .setExpiration(accessTokenExpiredTime)
                // HS256 알고리즘과 시크릿 키를 통해 시그니처를 생성함
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 토큰을 통해 사용자의 권한 정보를 반환하는 메서드
     * 이렇게 반환된 권한 정보는 JwtFilter.doFilter()를 통해 SecurityContextHolder에 저장된다
     */
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        // 권한 정보(Role)가 없는 사용자면 예외 발생
        if (claims.get(ROLE_CLAIM) == null) {
            throw new IllegalTokenException("권한 정보가 없는 토큰입니다.");
        }

        // 쉼표로 구분되어 있는 권한 정보를 쪼개서 배열로 만듦
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get(ROLE_CLAIM).toString().split(","))
                // 해당 hasRole이 권한 정보를 식별하기 위해 전처리 작업을 해줌
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                // 배열을 리스트로 변환해 반환함
                .toList();

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authorities);
        authentication.setDetails(claims);

        return authentication;
    }

    /**
     * HTTP 요청 메시지의 Authorization 헤더로부터 토큰을 분석해서 토큰을 반환하는 메서드.<br>
     * 토큰의 형식이 이상할 경우 null을 반환한다
     */
    public String resolveToken(HttpServletRequest request) {
        // AUTHORIZATION 헤더로부터 토큰을 받온다
        String bearerToken = request.getHeader(AUTHORIZATION);

        // null 검사 후, 토큰이 "Bearer "로 시작하는지 검사한다
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) {
            // 조건에 부합하다면 앞의 "Bearer "를 제거한 문자열(토큰)을 반환한다
            return bearerToken.substring(7);
        }

        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    // 시그니처 복호화에 사용할 SECRET KEY 지정
                    .setSigningKey(key)
                    .build()
                    // 토큰을 복호화함
                    .parseClaimsJws(token);
            // 예외가 발생하지 않았다면 적합한 토큰이므로 true를 반환함
            return true;
        } catch (UnsupportedJwtException | ExpiredJwtException | IllegalArgumentException e) {
            // 위 예외가 발생했다면 적합하지 않은 토큰이므로 false를 반환함
            return false;
        }
    }

    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    // 시그니처 복호화에 사용할 SECRET KEY 지정
                    .setSigningKey(key)
                    .build()
                    // 토큰을 복호화해서 클레임들을 얻음
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (SignatureException e) {
            throw new IllegalTokenException("토큰 복호화에 실패했습니다.");
        }
    }

    public String expireAccessToken(Member member) {
        Date accessTokenExpiredTime = new Date();

        return Jwts.builder()
                // 토큰 제목(sub 클레임)을 지정한다
                .setSubject(member.getId().toString())
                // 클레임을 추가함 - "Role":"멤버의 ID"
                .claim(ROLE_CLAIM, member.getRole())
                .setExpiration(accessTokenExpiredTime)
                // HS256 알고리즘과 시크릿 키를 통해 시그니처를 생성함
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
