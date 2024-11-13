package junwatson.mycreditcalculator.dto.response;

import junwatson.mycreditcalculator.domain.Lecture;
import junwatson.mycreditcalculator.domain.LectureType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class LectureInfoResponseDto {

    private Long id;
    private String name;
    private Double credit;
    private String major;
    private Integer semester;
    private LectureType type;

    public static LectureInfoResponseDto from(Lecture lecture) {
        return LectureInfoResponseDto.builder()
                .id(lecture.getId())
                .name(lecture.getName())
                .credit(lecture.getCredit())
                .major(lecture.getMajor())
                .semester(lecture.getSemester())
                .type(lecture.getType())
                .build();
    }
}
