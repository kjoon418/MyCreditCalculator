package junwatson.mycreditcalculator.service;

import junwatson.mycreditcalculator.domain.Lecture;
import junwatson.mycreditcalculator.domain.Member;
import junwatson.mycreditcalculator.dto.request.LectureRegistrationRequestDto;
import junwatson.mycreditcalculator.dto.response.LectureInfoResponseDto;
import junwatson.mycreditcalculator.repository.MemberRepository;
import junwatson.mycreditcalculator.repository.dao.LectureSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LectureService {

    private final MemberRepository repository;

    /**
     * 강의 조회 메서드
     */
    @Transactional(readOnly = true)
    public List<LectureInfoResponseDto> findLectures(String token) {
        Member member = repository.findMemberByAccessToken(token);
        List<Lecture> findLectures = repository.findLecturesByCondition(member, LectureSearchCondition.noCondition());
        List<LectureInfoResponseDto> result = new ArrayList<>();

        for (Lecture lecture : findLectures) {
            result.add(LectureInfoResponseDto.from(lecture));
        }

        return result;
    }

    /**
     * 강의 등록 메서드
     */
    public LectureInfoResponseDto registerLecture(String token, LectureRegistrationRequestDto lectureDto) {
        Member member = repository.findMemberByAccessToken(token);
        return repository.registerLecture(member, lectureDto);
    }

    /**
     * 강의 삭제 메서드
     */
    public LectureInfoResponseDto deleteLecture(String token, Long lectureId) {
        Member member = repository.findMemberByAccessToken(token);
        Lecture deletedLecture = repository.removeLectureById(member, lectureId);
        return LectureInfoResponseDto.from(deletedLecture);
    }
}