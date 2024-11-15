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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LectureDao {

    /**
     * id를 통해 강의를 삭제하는 메서드
     */
    public Lecture removeLectureById(Member member, Long lectureId) {
        log.info("LectureDao.removeLectureById() called");

        Lecture lecture = findLectureById(member, lectureId);
        member.getLectures().remove(lecture);

        return lecture;
    }

    /**
     * id를 통해 강의를 조회하는 메서드
     */
    public Lecture findLectureById(Member member, Long lectureId) {
        return member.getLectures().stream()
                .filter(lecture -> lecture.getId().equals(lectureId))
                .findAny()
                .orElseThrow(() -> new LectureNotExistException("해당 id를 가진 강의가 존재하지 않습니다."));
    }

    /**
     * 강의를 수정하는 메서드
     */
    public Lecture updateLecture(Lecture lecture, String name, Double credit, String major, Integer semester, LectureType type) {
        return lecture.update(name, credit, major, semester, type);
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
