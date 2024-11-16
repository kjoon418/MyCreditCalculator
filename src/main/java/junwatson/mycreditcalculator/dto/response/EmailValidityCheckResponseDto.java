package junwatson.mycreditcalculator.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class EmailValidityCheckResponseDto {

    private String email;

    public static EmailValidityCheckResponseDto from(String email) {
        return EmailValidityCheckResponseDto.builder()
                .email(email)
                .build();
    }
}
