package junwatson.mycreditcalculator.dto.request;

import junwatson.mycreditcalculator.domain.Member;
import junwatson.mycreditcalculator.domain.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
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
