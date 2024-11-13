package junwatson.mycreditcalculator.repository;

import jakarta.persistence.EntityManager;
import junwatson.mycreditcalculator.domain.Member;
import junwatson.mycreditcalculator.dto.request.ManagerSignUpRequestDto;
import junwatson.mycreditcalculator.dto.request.MemberSignInRequestDto;
import junwatson.mycreditcalculator.dto.request.MemberSignUpRequestDto;
import junwatson.mycreditcalculator.dto.token.TokenDto;
import junwatson.mycreditcalculator.exception.IllegalMemberStateException;
import junwatson.mycreditcalculator.exception.MemberNotExistException;
import junwatson.mycreditcalculator.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;
    private final TokenProvider tokenProvider;

    @Transactional
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
        Optional<Member> findMembers = findMemberByEmailAndPassword(member)
                .stream()
                .findFirst();
        Member findMember = findMembers.orElseThrow(MemberNotExistException::new);

        String accessToken = tokenProvider.createAccessToken(findMember);

        return TokenDto.builder()
                .accessToken(accessToken)
                .build();
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

        return findMemberByEmailAndPassword(member)
                .isEmpty();
    }

    @Transactional(readOnly = true)
    protected List<Member> findMemberByEmailAndPassword(Member member) {
        return em.createQuery("select m from Member m where m.email=:email and m.password=:password", Member.class)
                .setParameter("email", member.getEmail())
                .setParameter("password", member.getPassword())
                .getResultList();
    }
}
