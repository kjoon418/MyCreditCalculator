package junwatson.mycreditcalculator.dto.request;

import junwatson.mycreditcalculator.domain.Lecture;
import junwatson.mycreditcalculator.domain.LectureType;
import junwatson.mycreditcalculator.domain.Member;
import junwatson.mycreditcalculator.exception.lecture.IllegalLectureTypeException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class LectureRegistrationRequestDto {

    private String name;
    private Double credit;
    private String major;
    private Integer semester;
    private String type;

    public Lecture toEntityWithMember(Member member) {
        LectureType lectureType;
        switch (type) {
            case "MajorCourses":
                lectureType = LectureType.MajorCourses;
                break;
            case "GeneralEducationCourses":
                lectureType = LectureType.GeneralEducationCourses;
                break;
            default:
                throw new IllegalLectureTypeException("강의 등록 중 잘못된 LectureType 입력이 들어왔습니다.");
        }

        return Lecture.builder()
                .member(member)
                .name(name)
                .credit(credit)
                .major(major)
                .semester(semester)
                .type(lectureType)
                .build();
    }
}
