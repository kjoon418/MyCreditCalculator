package junwatson.mycreditcalculator.dto.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TokenDto {
    private String accessToken;

    public static TokenDto from(String token) {
        return TokenDto.builder()
                .accessToken(token)
                .build();
    }
}
