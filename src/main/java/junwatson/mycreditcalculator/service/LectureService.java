package junwatson.mycreditcalculator.service;

import junwatson.mycreditcalculator.domain.Lecture;
import junwatson.mycreditcalculator.domain.Member;
import junwatson.mycreditcalculator.dto.request.LectureRegistrationRequestDto;
import junwatson.mycreditcalculator.dto.request.LectureSearchRequestDto;
import junwatson.mycreditcalculator.dto.request.LectureUpdateRequestDto;
import junwatson.mycreditcalculator.dto.response.LectureInfoResponseDto;
import junwatson.mycreditcalculator.repository.MemberRepository;
import junwatson.mycreditcalculator.repository.dao.LectureSearchCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LectureService {

    private final MemberRepository repository;

    /**
     * 강의 전체 조회 메서드
     */
    @Transactional(readOnly = true)
    public List<LectureInfoResponseDto> findLectures(Long memberId) {
        log.info("LectureService.findLectures() called");

        Member member = repository.findMemberById(memberId);
        List<Lecture> findLectures = repository.findLecturesByCondition(member, LectureSearchCondition.noCondition());

        List<LectureInfoResponseDto> result = new ArrayList<>();
        for (Lecture lecture : findLectures) {
            result.add(LectureInfoResponseDto.from(lecture));
        }

        return result;
    }

    /**
     * 강의 조건 검색 메서드
     */
    @Transactional(readOnly = true)
    public List<LectureInfoResponseDto> searchLectures(Long memberId, LectureSearchRequestDto conditionDto) {
        log.info("LectureService.searchLectures() called");

        Member member = repository.findMemberById(memberId);
        LectureSearchCondition condition = conditionDto.toCondition();
        List<Lecture> findLectures = repository.findLecturesByCondition(member, condition);

        List<LectureInfoResponseDto> result = new ArrayList<>();
        for (Lecture lecture : findLectures) {
            result.add(LectureInfoResponseDto.from(lecture));
        }

        return result;
    }

    /**
     * 강의 등록 메서드
     */
    public LectureInfoResponseDto registerLecture(Long memberId, LectureRegistrationRequestDto lectureDto) {
        log.info("LectureService.registerLecture() called");

        Member member = repository.findMemberById(memberId);
        Lecture lecture = repository.registerLecture(member, lectureDto);

        return LectureInfoResponseDto.from(lecture);
    }

    /**
     * 강의 삭제 메서드
     */
    public LectureInfoResponseDto deleteLecture(Long memberId, Long lectureId) {
        log.info("LectureService.deleteLecture() called");

        Member member = repository.findMemberById(memberId);
        Lecture deletedLecture = repository.removeLectureById(member, lectureId);

        return LectureInfoResponseDto.from(deletedLecture);
    }

    /**
     * 강의 수정 메서드
     */
    public LectureInfoResponseDto updateLecture(Long memberId, LectureUpdateRequestDto lectureDto) {
        log.info("LectureService.updateLecture() called");

        Member member = repository.findMemberById(memberId);
        Lecture lecture = repository.updateLecture(member, lectureDto);

        return LectureInfoResponseDto.from(lecture);
    }
}
