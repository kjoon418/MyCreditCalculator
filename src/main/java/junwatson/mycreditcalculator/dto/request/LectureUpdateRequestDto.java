package junwatson.mycreditcalculator.dto.request;

import junwatson.mycreditcalculator.domain.LectureType;
import lombok.Getter;

@Getter
public class LectureUpdateRequestDto {

    private Long id;
    private String name;
    private Double credit;
    private String major;
    private Integer semester;
    private LectureType type;

}
