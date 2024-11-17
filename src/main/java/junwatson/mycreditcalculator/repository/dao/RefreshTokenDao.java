package junwatson.mycreditcalculator.repository.dao;

import junwatson.mycreditcalculator.domain.Member;
import junwatson.mycreditcalculator.domain.RefreshToken;
import junwatson.mycreditcalculator.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefreshTokenDao {

    private final TokenProvider tokenProvider;

    public boolean isValidateRefreshToken(Member member, String refreshTokenString) {
        return member.getRefreshToken()
                .getToken()
                .equals(refreshTokenString);
    }

    public RefreshToken createRefreshToken(Member member) {
        String tokenString = tokenProvider.createRefreshToken(member);
        RefreshToken refreshToken = RefreshToken.builder()
                .member(member)
                .token(tokenString)
                .build();
        member.setRefreshToken(refreshToken);

        return refreshToken;
    }
}
