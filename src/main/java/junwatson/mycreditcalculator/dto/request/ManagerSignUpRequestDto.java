package junwatson.mycreditcalculator.dto.request;

import junwatson.mycreditcalculator.domain.Member;
import junwatson.mycreditcalculator.domain.MemberRole;
import lombok.Getter;

@Getter
public class ManagerSignUpRequestDto {

    private String email;
    private String password;
    private String name;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .role(MemberRole.MANAGER)
                .build();
    }
}
