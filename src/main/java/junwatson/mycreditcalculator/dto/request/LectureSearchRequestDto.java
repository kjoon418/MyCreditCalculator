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
    Boolean majorOnly;

    public LectureSearchCondition toCondition() {
        boolean isMajorOnly = majorOnly != null && majorOnly;

        return LectureSearchCondition.builder()
                .semester(semester)
                .majorOnly(isMajorOnly)
                .build();
    }
}
