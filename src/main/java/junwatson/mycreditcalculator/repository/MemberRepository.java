package junwatson.mycreditcalculator.repository;

import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityManager;
import junwatson.mycreditcalculator.domain.Lecture;
import junwatson.mycreditcalculator.domain.Member;
import junwatson.mycreditcalculator.dto.request.LectureRegistrationRequestDto;
import junwatson.mycreditcalculator.dto.request.MemberSignInRequestDto;
import junwatson.mycreditcalculator.dto.request.MemberSignUpRequestDto;
import junwatson.mycreditcalculator.dto.response.LectureInfoResponseDto;
import junwatson.mycreditcalculator.dto.token.TokenDto;
import junwatson.mycreditcalculator.exception.IllegalMemberStateException;
import junwatson.mycreditcalculator.exception.MemberNotExistException;
import junwatson.mycreditcalculator.jwt.TokenProvider;
import junwatson.mycreditcalculator.repository.dao.LectureDao;
import junwatson.mycreditcalculator.repository.dao.LectureSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional
public class MemberRepository {

    private final EntityManager em;
    private final TokenProvider tokenProvider;
    private final LectureDao lectureDao;

    public Member signUp(MemberSignUpRequestDto memberSignUpRequestDto) {
        Member member = memberSignUpRequestDto.toEntity();

        if (validate(member)) {
            em.persist(member);
        } else {
            throw new IllegalMemberStateException("적합하지 않은 회원 정보입니다.");
        }

        return member;
    }

    @Transactional(readOnly = true)
    public TokenDto signIn(MemberSignInRequestDto memberSignInRequestDto) {
        Member member = memberSignInRequestDto.toEntity();
        Optional<Member> findMembers = findMemberByEmailAndPassword(member.getEmail(), member.getPassword())
                .stream()
                .findFirst();
        Member findMember = findMembers.orElseThrow(MemberNotExistException::new);

        String accessToken = tokenProvider.createAccessToken(findMember);

        return TokenDto.builder()
                .accessToken(accessToken)
                .build();
    }

    /**
     * 강의를 조회하는 메서드
     */
    public List<Lecture> findLecturesByCondition(Member member, LectureSearchCondition condition) {
        return lectureDao.findLecturesByMember(member, condition);
    }

    /**
     * 강의를 등록하는 메서드
     */
    public LectureInfoResponseDto registerLecture(Member member, LectureRegistrationRequestDto lectureDto) {
        Lecture lecture = lectureDto.toEntityWithMember(member);
        member.getLectures().add(lecture);

        return LectureInfoResponseDto.from(lecture);
    }

    /**
     * JWT를 통해 Member를 조회하는 메서드
     */
    @Transactional(readOnly = true)
    public Member findMemberByAccessToken(String accessToken) {
        Claims claims = tokenProvider.parseClaims(accessToken);
        int id = Integer.parseInt(claims.getSubject());
        return em.find(Member.class, id);
    }

    /**
     * 해당 회원 정보로 회원가입이 가능한지 유효성을 검사하는 메서드.
     */
    @Transactional(readOnly = true)
    protected boolean validate(Member member) {
        if (member == null ||
                member.getEmail() == null ||
                member.getName() == null ||
                member.getPassword() == null) {
            return false;
        }

        // 해당 이메일로 가입한 회원이 이미 있는지 검사
        return findMemberByEmail(member.getEmail()).isEmpty();
    }

    /**
     * 일치하는 이메일을 가진 데이터가 DB에 있는지 조회하는 메서드
     */
    @Transactional(readOnly = true)
    protected List<Member> findMemberByEmail(String email) {
        String jpql = "select m from Member m where m.email=:email";
        return em.createQuery(jpql, Member.class)
                .setParameter("email", email)
                .getResultList();
    }

    /**
     * 이메일과 비밀번호 조합과 일치하는 데이터가 DB에 있는지 조회하는 메서드
     */
    @Transactional(readOnly = true)
    protected List<Member> findMemberByEmailAndPassword(String email, String password) {
        return em.createQuery("select m from Member m where m.email=:email and m.password=:password", Member.class)
                .setParameter("email", email)
                .setParameter("password", password)
                .getResultList();
    }
}
