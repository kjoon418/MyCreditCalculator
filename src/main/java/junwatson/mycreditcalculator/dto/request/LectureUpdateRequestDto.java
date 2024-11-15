package junwatson.mycreditcalculator.dto.request;

import junwatson.mycreditcalculator.domain.LectureType;
import junwatson.mycreditcalculator.exception.lecture.IllegalLectureTypeException;
import lombok.Getter;

@Getter
public class LectureUpdateRequestDto {

    private Long id;
    private String name;
    private Double credit;
    private String major;
    private Integer semester;
    private String type;

    public LectureType getType() {
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

        return lectureType;
    }
}
