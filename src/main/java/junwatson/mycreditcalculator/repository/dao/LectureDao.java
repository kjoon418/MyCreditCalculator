package junwatson.mycreditcalculator.repository.dao;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityManager;
import junwatson.mycreditcalculator.domain.Lecture;
import junwatson.mycreditcalculator.domain.LectureType;
import junwatson.mycreditcalculator.domain.Member;
import junwatson.mycreditcalculator.exception.IllegalMemberStateException;
import junwatson.mycreditcalculator.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static junwatson.mycreditcalculator.domain.QLecture.*;

@Repository
@Transactional
public class LectureDao {

    private final EntityManager em;
    private final JPAQueryFactory query;
    private final TokenProvider tokenProvider;

    @Autowired
    public LectureDao(EntityManager em, TokenProvider tokenProvider) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
        this.tokenProvider = tokenProvider;
    }

    /**
     * id를 통해 강의를 삭제하는 메서드
     * 다른 Member의 강의를 삭제하려 시도한 경우 예외를 발생시킴
     */
    public Lecture removeLectureById(Member member, Long lectureId) {
        Lecture lecture = em.find(Lecture.class, lectureId);
        if (lecture.getMember() != member) {
            throw new IllegalMemberStateException("다른 회원의 강의는 삭제할 수 없습니다.");
        }
        em.remove(lecture);

        return lecture;
    }

    /**
     * 해당 회원의 전체 강의를 조건에 따라 조회하는 메서드.
     */
    public List<Lecture> findLecturesByMember(Member member, LectureSearchCondition condition) {
        Integer semester = condition.getSemester();
        boolean majorOnly = condition.isMajorOnly();

        Stream<Lecture> stream = member.getLectures().stream();

        if (majorOnly) {
            stream = stream.filter(lecture -> lecture.getType() == LectureType.MajorCourses);
        }
        if (semester != null) {
            stream = stream.filter(lecture -> lecture.getSemester().equals(semester));
        }

        return stream.collect(Collectors.toList());
    }

    private BooleanExpression sameMember(Member member) {
        if (member == null || member.getId() == null) {
            throw new IllegalMemberStateException("강의 검색에 실패했습니다: Member 혹은 Member의 Id는 null일 수 없습니다.");
        }

        return lecture.member.eq(member);
    }

    private BooleanExpression onlyMajor(boolean isMajorOnly) {
        if (!isMajorOnly) {
            return null;
        }

        return lecture.type.eq(LectureType.MajorCourses);
    }

    private BooleanExpression selectSemester(Integer semester) {
        if (semester == null) {
            return null;
        }

        return lecture.semester.eq(semester);
    }
}
