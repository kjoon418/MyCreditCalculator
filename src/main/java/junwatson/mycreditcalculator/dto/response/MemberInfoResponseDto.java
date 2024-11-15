package junwatson.mycreditcalculator.dto.response;

import junwatson.mycreditcalculator.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class MemberInfoResponseDto {

    private String name;

    public static MemberInfoResponseDto from(Member member) {
        return MemberInfoResponseDto.builder()
                .name(member.getName())
                .build();
    }
}
