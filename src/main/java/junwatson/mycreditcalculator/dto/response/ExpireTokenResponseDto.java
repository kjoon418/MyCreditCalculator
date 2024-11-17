package junwatson.mycreditcalculator.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ExpireTokenResponseDto {

    private String accessToken;

    public static ExpireTokenResponseDto of(String accessToken) {
        return ExpireTokenResponseDto.builder()
                .accessToken(accessToken)
                .build();
    }
}
