package junwatson.mycreditcalculator.dto.request;

import junwatson.mycreditcalculator.domain.Member;
import lombok.Getter;

@Getter
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
