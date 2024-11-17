package junwatson.mycreditcalculator.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReissueTokenResponseDto {

    private String accessToken;

    public static ReissueTokenResponseDto from(String accessToken) {
        return ReissueTokenResponseDto.builder()
                .accessToken(accessToken)
                .build();
    }
}
