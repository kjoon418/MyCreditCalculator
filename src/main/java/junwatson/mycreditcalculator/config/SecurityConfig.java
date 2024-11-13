package junwatson.mycreditcalculator.config;

import junwatson.mycreditcalculator.jwt.JwtFilter;
import junwatson.mycreditcalculator.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // JWT를 사용하므로, HTTP 기본 인증을 비활성화함
                .httpBasic(AbstractHttpConfigurer::disable)
                // RESTful API와 Stateless 인증을 사용하므로, CSRF 방어가 불필요하므로 CSRF 보호를 비활성화함
                .csrf(AbstractHttpConfigurer::disable)
                // JWT를 사용해서 인증 상태를 별도로 관리할 것이므로, 세션 상태를 비저장으로 설정하여 서버에서 세션을 사용하지 않게 함
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))
                // JWT를 사용한 인증이므로 별도의 로그인 폼을 사용하지 않음
                .formLogin(AbstractHttpConfigurer::disable)
                // /authorization/~ 경로에 대한 접근은 인증 없이 허용하고, 그 외 경로는 인증된 사용자만 접근하도록 함
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/authorization/**").permitAll()
                        .anyRequest().authenticated())
                // 요청이 들어올 때 마다 JwtFilter 클래스를 이용해서 JWT 토큰을 검증하고 인증 정보를 설정하도록 함
                // UsernamePasswordAuthenticationFilter 전에 JwtFilter가 동작하도록 함
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
