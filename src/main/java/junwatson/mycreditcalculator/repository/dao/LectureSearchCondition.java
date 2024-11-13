package junwatson.mycreditcalculator.repository.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 강의 검색을 위한 조건을 담는 객체
 */
@Getter
@AllArgsConstructor
@Builder
public class LectureSearchCondition {
    Integer semester;
    boolean majorOnly;

    /**
     * 아무 조건도 걸고 싶지 않을 때(전체 강의를 모두 조회하고 싶을 때) 사용하는 메서드
     */
    public static LectureSearchCondition noCondition() {
        return LectureSearchCondition.builder()
                .majorOnly(false)
                .build();
    }
}
