package junwatson.mycreditcalculator.dto.request;

import junwatson.mycreditcalculator.repository.dao.LectureSearchCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class LectureSearchRequestDto {
    Integer semester;
    boolean majorOnly;

    public LectureSearchCondition toCondition() {
        return LectureSearchCondition.builder()
                .semester(semester)
                .majorOnly(majorOnly)
                .build();
    }
}
