package junwatson.mycreditcalculator.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateTokenResponseDto {

    private String accessToken;
    private String refreshToken;

    public static CreateTokenResponseDto of(String accessToken, String refreshToken) {
        return CreateTokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
