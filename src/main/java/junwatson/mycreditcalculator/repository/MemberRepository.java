package junwatson.mycreditcalculator.repository;

import jakarta.persistence.EntityManager;
import junwatson.mycreditcalculator.domain.Lecture;
import junwatson.mycreditcalculator.domain.Member;
import junwatson.mycreditcalculator.dto.request.LectureRegistrationRequestDto;
import junwatson.mycreditcalculator.dto.request.LectureUpdateRequestDto;
import junwatson.mycreditcalculator.dto.request.MemberSignInRequestDto;
import junwatson.mycreditcalculator.dto.request.MemberSignUpRequestDto;
import junwatson.mycreditcalculator.dto.token.TokenDto;
import junwatson.mycreditcalculator.exception.member.IllegalMemberStateException;
import junwatson.mycreditcalculator.exception.member.MemberNotExistException;
import junwatson.mycreditcalculator.jwt.TokenProvider;
import junwatson.mycreditcalculator.repository.dao.LectureDao;
import junwatson.mycreditcalculator.repository.dao.LectureSearchCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@Slf4j
public class MemberRepository {

    private final EntityManager em;
    private final TokenProvider tokenProvider;
    private final LectureDao lectureDao;
    private final HashSet<Character> ALLOWED_WORDS = new HashSet<>();

    @Autowired
    public MemberRepository(EntityManager em, TokenProvider tokenProvider, LectureDao lectureDao) {
        this.em = em;
        this.tokenProvider = tokenProvider;
        this.lectureDao = lectureDao;

        char[] allowedWordsArr = {'!', '@', '#', '$', '%', '^', '&', '*', '.'};

        for (char word : allowedWordsArr) {
            ALLOWED_WORDS.add(word);
        }
    }

    /**
     * 회원 가입 메서드
     */
    public Member signUp(MemberSignUpRequestDto memberSignUpRequestDto) {
        log.info("MemberRepository.signUp() called");

        Member member = memberSignUpRequestDto.toEntity();

        if (signUpValidate(member.getEmail(), member.getPassword(), member.getName())) {
            em.persist(member);
        } else {
            throw new IllegalMemberStateException("적합하지 않은 회원 정보입니다.");
        }

        return member;
    }

    /**
     * 로그인 메서드
     */
    @Transactional(readOnly = true)
    public TokenDto signIn(MemberSignInRequestDto memberSignInRequestDto) {
        log.info("MemberRepository.signIn() called");

        Member member = memberSignInRequestDto.toEntity();
        Optional<Member> findMembers = findMemberByEmailAndPassword(member.getEmail(), member.getPassword())
                .stream()
                .findFirst();
        Member findMember = findMembers.orElseThrow(() -> new MemberNotExistException("아이디 혹은 비밀번호가 틀립니다."));

        String accessToken = tokenProvider.createAccessToken(findMember);

        return TokenDto.builder()
                .accessToken(accessToken)
                .build();
    }

    /**
     * 멤버 삭제 메서드
     */
    public Member removeMemberById(Long memberId) {
        log.info("MemberRepository.removeMemberById() called");

        Member member = em.find(Member.class, memberId);
        if (member == null) {
            throw new MemberNotExistException("회원이 존재하지 않아 삭제에 실패했습니다.");
        }
        em.remove(member);

        return member;
    }

    /**
     * 멤버 이메일 수정 메서드
     */
    public Member updateMemberEmail(Member member, String newEmail) {
        log.info("MemberRepository.updateMemberEmail() called");

        if (!validate(newEmail, member.getPassword(), member.getName())) {
            throw new IllegalMemberStateException("해당 이메일을 이미 사용 중이거나, 이메일의 형식이 올바르지 않습니다.");
        }

        return member.update(newEmail, member.getPassword(), member.getName());
    }

    /**
     * 멤버 비밀번호 수정 메서드
     */
    public Member updateMemberPassword(Member member, String newPassword) {
        log.info("MemberRepository.updateMemberPassword() called");

        if (!validate(member.getEmail(), newPassword, member.getName())) {
            throw new IllegalMemberStateException("비밀번호의 형식이 올바르지 않습니다.");
        }

        return member.update(member.getEmail(), newPassword, member.getName());
    }

    /**
     * 멤버 이름 수정 메서드
     */
    public Member updateMemberName(Member member, String newName) {
        log.info("MemberRepository.updateMemberName() called");

        if (!validate(member.getEmail(), member.getPassword(), newName)) {
            throw new IllegalMemberStateException("이름의 형식이 올바르지 않습니다.");
        }

        return member.update(member.getEmail(), member.getPassword(), newName);
    }

    /**
     * 전체 강의를 조회하는 메서드
     */
    public List<Lecture> findLecturesByCondition(Member member, LectureSearchCondition condition) {
        log.info("MemberRepository.findLecturesByCondition() called");

        return lectureDao.findLecturesByMember(member, condition);
    }

    /**
     * PK를 통해 Member를 조회하는 메서드
     */
    @Transactional(readOnly = true)
    public Member findMemberById(Long id) {
        log.info("MemberRepository.findMemberById() called");

        Member member = em.find(Member.class, id);
        if (member == null) {
            throw new MemberNotExistException("해당 id를 가진 멤버가 없습니다.");
        }

        return member;
    }

    /**
     * 강의를 등록하는 메서드
     */
    public Lecture registerLecture(Member member, LectureRegistrationRequestDto lectureDto) {
        log.info("MemberRepository.registerLecture() called");

        Lecture lecture = lectureDto.toEntityWithMember(member);
        member.getLectures().add(lecture);
        // lecture 엔티티의 id값이 null인 상태로 DTO 객체를 만들지 않기 위해 플러시함
        em.flush();

        return lecture;
    }

    /**
     * 강의를 id를 통해 삭제하는 메서드
     * 다른 멤버의 강의는 삭제하지 못하게 함
     */
    public Lecture removeLectureById(Member member, Long lectureId) {
        log.info("MemberRepository.removeLectureById() called");

        return lectureDao.removeLectureById(member, lectureId);
    }

    /**
     * 강의 수정 메서드
     */
    public Lecture updateLecture(Member member, LectureUpdateRequestDto lectureDto) {
        log.info("MemberRepository.updateLecture() called");

        Lecture lecture = lectureDao.findLectureById(member, lectureDto.getId());

        return lectureDao.updateLecture(
                lecture,
                lectureDto.getName(),
                lectureDto.getCredit(),
                lectureDto.getMajor(),
                lectureDto.getSemester(),
                lectureDto.getType());
    }

    /**
     * 해당 회원 정보로 회원가입이 가능한지 유효성을 검사하는 메서드.
     */
    private boolean signUpValidate(String email, String password, String name) {
        log.info("MemberRepository.signUpValidate() called");

        validate(email, password, name);

        // 해당 이메일로 가입한 회원이 이미 있는지 검사
        return findMemberByEmail(email).isEmpty();
    }

    /**
     * 해당 회원 정보로 회원 수정이 가능한지 유효성을 검사하는 메서드.
     */
    private boolean validate(String email, String password, String name) {
        log.info("MemberRepository.validate() called");

        // 이메일, 이름, 패스워드중 하나라도 null 있다면 불가능
        if (!StringUtils.hasText(email) &&
                !StringUtils.hasText(password) &&
                !StringUtils.hasText(name)) {
            return false;
        }

        // 공백이 들어가면 불가능
        if (email.contains(" ") ||
                password.contains(" ") ||
                name.contains(" ")) {
            return false;
        }

        // 영어나 숫자가 아니면서, 허용되지 않은 문자가 들어가면 불가능
        return !isIllegalString(email) &&
                !isIllegalString(password) &&
                !isIllegalString(name);
    }

    /**
     * 해당 String이 영어나 숫자가 아니면서 허용되지 않은 문자를 포함하는지 확인하는 메서드
     */
    private boolean isIllegalString(String string) {
        for (int i = 0; i < string.length(); i++) {
            char word = string.charAt(i);
            if (Character.isAlphabetic(word)) {
                continue;
            }
            if (Character.isDigit(word)) {
                continue;
            }
            if (!ALLOWED_WORDS.contains(word)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 일치하는 이메일을 가진 데이터가 DB에 있는지 조회하는 메서드
     */
    @Transactional(readOnly = true)
    protected List<Member> findMemberByEmail(String email) {
        log.info("MemberRepository.findMemberByEmail() called");

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
        log.info("MemberRepository.findMemberByEmailAndPassword() called");

        return em.createQuery("select m from Member m where m.email=:email and m.password=:password", Member.class)
                .setParameter("email", email)
                .setParameter("password", password)
                .getResultList();
    }
}
