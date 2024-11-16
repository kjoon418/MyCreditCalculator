package junwatson.mycreditcalculator.service;

import junwatson.mycreditcalculator.domain.Member;
import junwatson.mycreditcalculator.dto.request.*;
import junwatson.mycreditcalculator.dto.response.EmailValidityCheckResponseDto;
import junwatson.mycreditcalculator.dto.response.MemberInfoResponseDto;
import junwatson.mycreditcalculator.dto.token.TokenDto;
import junwatson.mycreditcalculator.exception.member.IllegalMemberStateException;
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
@Transactional
public class MemberService {

    private final MemberRepository repository;
    private final TokenProvider tokenProvider;


    public TokenDto signUp(MemberSignUpRequestDto memberSignUpRequestDto) {
        log.info("MemberService.signUp() called");

        Member member = repository.signUp(memberSignUpRequestDto);
        String accessToken = tokenProvider.createAccessToken(member);

        return TokenDto.from(accessToken);
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
            throw new MemberNotExistException("회원이 조회되지 않습니다.");
        }

        return member;
    }

    @Transactional(readOnly = true)
    public EmailValidityCheckResponseDto emailValidityCheck(EmailValidityCheckRequestDto requestDto) {
        log.info("MemberService.isEmailExists() called");

        String email = requestDto.getEmail();

        if (repository.isIllegalString(email)) {
            throw new IllegalMemberStateException("유효하지 않은 이메일입니다.");
        }

        if (!repository.findMemberByEmail(email).isEmpty()) {
            throw new IllegalMemberStateException("이미 존재하는 이메일입니다.");
        }

        return EmailValidityCheckResponseDto.from(email);
    }

    public TokenDto deleteMemberById(Long memberId) {
        log.info("MemberService.deleteMemberById() called");

        Member member = repository.removeMemberById(memberId);
        String accessToken = tokenProvider.expireAccessToken(member);

        return TokenDto.from(accessToken);
    }

    public MemberInfoResponseDto updateMemberEmail(Long memberId, MemberUpdateEmailRequestDto memberDto) {
        log.info("MemberService.updateMemberEmail() called");

        Member member = repository.findMemberById(memberId);
        Member updatedMember = repository.updateMemberEmail(member, memberDto.getEmail());

        return MemberInfoResponseDto.from(updatedMember);
    }

    public MemberInfoResponseDto updateMemberPassword(Long memberId, MemberUpdatePasswordRequestDto memberDto) {
        log.info("MemberService.updateMemberPassword() called");

        Member member = repository.findMemberById(memberId);
        Member updatedMember = repository.updateMemberPassword(member, memberDto.getPassword());

        return MemberInfoResponseDto.from(updatedMember);
    }

    public MemberInfoResponseDto updateMemberName(Long memberId, MemberUpdateNameRequestDto memberDto) {
        log.info("MemberService.updateMemberName() called");

        Member member = repository.findMemberById(memberId);
        Member updatedMember = repository.updateMemberName(member, memberDto.getName());

        return MemberInfoResponseDto.from(updatedMember);
    }
}
