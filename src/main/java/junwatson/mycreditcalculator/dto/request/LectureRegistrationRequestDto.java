package junwatson.mycreditcalculator.dto.request;

import junwatson.mycreditcalculator.domain.Lecture;
import junwatson.mycreditcalculator.domain.LectureType;
import junwatson.mycreditcalculator.domain.Member;
import lombok.Getter;

@Getter
public class LectureRegistrationRequestDto {

    private String name;
    private Double credit;
    private String major;
    private Integer semester;
    private LectureType type;

    public Lecture toEntityWithMember(Member member) {
        return Lecture.builder()
                .member(member)
                .name(name)
                .credit(credit)
                .major(major)
                .semester(semester)
                .type(type)
                .build();
    }
}
