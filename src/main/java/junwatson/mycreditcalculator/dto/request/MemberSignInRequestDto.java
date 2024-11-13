package junwatson.mycreditcalculator.dto.request;

import junwatson.mycreditcalculator.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MemberSignInRequestDto {
    private String email;
    private String password;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .build();
    }
}
