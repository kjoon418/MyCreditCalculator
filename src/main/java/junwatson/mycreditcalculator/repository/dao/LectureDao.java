package junwatson.mycreditcalculator.repository.dao;

import jakarta.persistence.EntityManager;
import junwatson.mycreditcalculator.domain.Lecture;
import junwatson.mycreditcalculator.domain.LectureType;
import junwatson.mycreditcalculator.domain.Member;
import junwatson.mycreditcalculator.exception.lecture.LectureNotExistException;
import junwatson.mycreditcalculator.exception.member.IllegalMemberStateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LectureDao {

    private final EntityManager em;

    /**
     * id를 통해 강의를 삭제하는 메서드
     * 다른 Member의 강의를 삭제하려 시도한 경우 예외를 발생시킴
     */
    public Lecture removeLectureById(Member member, Long lectureId) {
        log.info("LectureDao.removeLectureById() called");

        Lecture lecture = em.find(Lecture.class, lectureId);
        if (lecture == null) {
            throw new LectureNotExistException("강의 조회에 실패했습니다.");
        }
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
        log.info("LectureDao.findLecturesByMember() called");

        Integer semester = condition.getSemester();
        boolean majorOnly = condition.isMajorOnly();

        Stream<Lecture> stream = member.getLectures().stream();

        if (majorOnly) {
            stream = stream.filter(lecture -> lecture.getType() == LectureType.MajorCourses);
        }
        if (semester != null) {
            stream = stream.filter(lecture -> lecture.getSemester().equals(semester));
        }
        List<Lecture> result = stream.collect(Collectors.toList());
        if (result.isEmpty()) {
            throw new LectureNotExistException("해당 조건으로 조회된 강의가 없습니다.");
        }

        return result;
    }
}
