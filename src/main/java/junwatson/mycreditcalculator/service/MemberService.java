package junwatson.mycreditcalculator.service;

import junwatson.mycreditcalculator.domain.Member;
import junwatson.mycreditcalculator.dto.request.MemberSignInRequestDto;
import junwatson.mycreditcalculator.dto.request.MemberSignUpRequestDto;
import junwatson.mycreditcalculator.dto.token.TokenDto;
import junwatson.mycreditcalculator.exception.member.MemberNotExistException;
import junwatson.mycreditcalculator.jwt.TokenProvider;
import junwatson.mycreditcalculator.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository repository;
    private final TokenProvider tokenProvider;

    @Transactional
    public TokenDto signUp(MemberSignUpRequestDto memberSignUpRequestDto) {
        log.info("MemberService.signUp() called");

        Member member = repository.signUp(memberSignUpRequestDto);
        String accessToken = tokenProvider.createAccessToken(member);

        return TokenDto.builder()
                .accessToken(accessToken)
                .build();
    }

    @Transactional(readOnly = true)
    public TokenDto signIn(MemberSignInRequestDto memberSignInRequestDto) {
        log.info("MemberService.signIn() called");

        return repository.signIn(memberSignInRequestDto);
    }

    @Transactional(readOnly = true)
    public Member findMemberById(Long memberId) {
        log.info("MemberService.findMemberById() called");

        Member member = repository.findMemberById(memberId);
        if (member == null) {
            throw new MemberNotExistException("멤버가 조회되지 않습니다.");
        }

        return member;
    }
}
