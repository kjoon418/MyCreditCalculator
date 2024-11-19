package junwatson.mycreditcalculator.repository.dao;

import junwatson.mycreditcalculator.domain.Member;
import junwatson.mycreditcalculator.domain.RefreshToken;
import junwatson.mycreditcalculator.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RefreshTokenDao {

    private final TokenProvider tokenProvider;

    public boolean isValidateRefreshToken(Member member, String refreshTokenString) {
        log.info("RefreshTokenDao.isValidateRefreshToken() called");

        if (member.getRefreshToken() == null) {
            return false;
        }

        return member.getRefreshToken()
                .getToken()
                .equals(refreshTokenString);
    }

    public RefreshToken createRefreshToken(Member member) {
        log.info("RefreshTokenDao.createRefreshToken() called");

        String tokenString = tokenProvider.createRefreshToken(member);
        RefreshToken refreshToken = RefreshToken.builder()
                .member(member)
                .token(tokenString)
                .build();
        member.setRefreshToken(refreshToken);

        return refreshToken;
    }
}
