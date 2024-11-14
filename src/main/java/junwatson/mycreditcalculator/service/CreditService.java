package junwatson.mycreditcalculator.service;

import junwatson.mycreditcalculator.domain.Lecture;
import junwatson.mycreditcalculator.domain.Member;
import junwatson.mycreditcalculator.dto.request.LectureSearchRequestDto;
import junwatson.mycreditcalculator.repository.MemberRepository;
import junwatson.mycreditcalculator.repository.dao.LectureSearchCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CreditService {

    private final MemberRepository repository;

    public Double calculateTotalCredit(Long memberId) {
        log.info("CreditService.calculateTotalCredit() called");

        Member member = repository.findMemberById(memberId);
        List<Lecture> lectures = repository.findLecturesByCondition(member, LectureSearchCondition.noCondition());

        return calculateAverageCredit(lectures);
    }

    public Double calculateCreditWithCondition(Long memberId, LectureSearchRequestDto conditionDto) {
        log.info("CreditService.calculateCreditWithCondition() called");

        Member member = repository.findMemberById(memberId);
        List<Lecture> lectures = repository.findLecturesByCondition(member, conditionDto.toCondition());

        return calculateAverageCredit(lectures);
    }

    private double calculateAverageCredit(List<Lecture> lectures) {
        log.info("CreditService.calculateAverageCredit() called");

        double sum = 0;
        int count = 0;
        for (Lecture lecture : lectures) {
            Double credit = lecture.getCredit();
            if (credit == null) {
                continue;
            }

            sum += credit;
            count++;
        }

        return sum / count;
    }
}
