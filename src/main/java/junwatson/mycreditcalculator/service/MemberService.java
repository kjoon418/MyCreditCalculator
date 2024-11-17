package junwatson.mycreditcalculator.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import junwatson.mycreditcalculator.domain.Member;
import junwatson.mycreditcalculator.domain.RefreshToken;
import junwatson.mycreditcalculator.dto.request.*;
import junwatson.mycreditcalculator.dto.response.*;
import junwatson.mycreditcalculator.exception.member.IllegalMemberStateException;
import junwatson.mycreditcalculator.exception.member.MemberNotExistException;
import junwatson.mycreditcalculator.exception.token.IllegalTokenException;
import junwatson.mycreditcalculator.jwt.TokenProvider;
import junwatson.mycreditcalculator.jwt.TokenType;
import junwatson.mycreditcalculator.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    public CreateTokenResponseDto signUp(MemberSignUpRequestDto memberSignUpRequestDto) {
        log.info("MemberService.signUp() called");

        Member member = memberRepository.signUp(memberSignUpRequestDto);
        String accessToken = memberRepository.createAccessToken(member);
        RefreshToken refreshToken = memberRepository.createRefreshToken(member);
        member.setRefreshToken(refreshToken);

        return CreateTokenResponseDto.of(accessToken, refreshToken.getToken());
    }

    @Transactional(readOnly = true)
    public CreateTokenResponseDto signIn(MemberSignInRequestDto memberSignInRequestDto) {
        log.info("MemberService.signIn() called");

        return memberRepository.signIn(memberSignInRequestDto);
    }

    @Transactional(readOnly = true)
    public Member findMemberById(Long memberId) {
        log.info("MemberService.findMemberById() called");

        Member member = memberRepository.findMemberById(memberId);
        if (member == null) {
            throw new MemberNotExistException("회원이 조회되지 않습니다.");
        }

        return member;
    }

    public ReissueTokenResponseDto reissueAccessToken(HttpServletRequest request) {
        log.info("MemberService.reissueAccessToken() called");

        // 리프레쉬 토큰의 유효성 검사
        String token = tokenProvider.resolveToken(request);
        if (tokenProvider.validateToken(token) && tokenProvider.hasProperTokenType(token, TokenType.REFRESH)) {
            Claims claims = tokenProvider.parseClaims(token);
            long memberId = Long.parseLong(claims.getSubject());
            Member member = memberRepository.findMemberById(memberId);

            String accessToken = tokenProvider.createAccessToken(member);

            return ReissueTokenResponseDto.from(accessToken);
        }

        throw new IllegalTokenException("유효하지 않은 리프레시 토큰입니다.");
    }

    public ExpireTokenResponseDto expireTokens(Long memberId) {
        Member member = memberRepository.findMemberById(memberId);
        String expiredToken = tokenProvider.expireToken(member);
        member.removeRefreshToken();

        return ExpireTokenResponseDto.of(expiredToken);
    }

    @Transactional(readOnly = true)
    public EmailValidityCheckResponseDto emailValidityCheck(EmailValidityCheckRequestDto requestDto) {
        log.info("MemberService.isEmailExists() called");

        String email = requestDto.getEmail();

        if (memberRepository.isIllegalString(email)) {
            throw new IllegalMemberStateException("유효하지 않은 이메일입니다.");
        }

        if (!memberRepository.findMemberByEmail(email).isEmpty()) {
            throw new IllegalMemberStateException("이미 존재하는 이메일입니다.");
        }

        return EmailValidityCheckResponseDto.from(email);
    }

    public ExpireTokenResponseDto deleteMemberById(Long memberId) {
        log.info("MemberService.deleteMemberById() called");

        Member member = memberRepository.removeMemberById(memberId);
        String expiredToken = tokenProvider.expireToken(member);

        return ExpireTokenResponseDto.of(expiredToken);
    }

    public MemberInfoResponseDto updateMemberEmail(Long memberId, MemberUpdateEmailRequestDto memberDto) {
        log.info("MemberService.updateMemberEmail() called");

        Member member = memberRepository.findMemberById(memberId);
        Member updatedMember = memberRepository.updateMemberEmail(member, memberDto.getEmail());

        return MemberInfoResponseDto.from(updatedMember);
    }

    public MemberInfoResponseDto updateMemberPassword(Long memberId, MemberUpdatePasswordRequestDto memberDto) {
        log.info("MemberService.updateMemberPassword() called");

        Member member = memberRepository.findMemberById(memberId);
        Member updatedMember = memberRepository.updateMemberPassword(member, memberDto.getPassword());

        return MemberInfoResponseDto.from(updatedMember);
    }

    public MemberInfoResponseDto updateMemberName(Long memberId, MemberUpdateNameRequestDto memberDto) {
        log.info("MemberService.updateMemberName() called");

        Member member = memberRepository.findMemberById(memberId);
        Member updatedMember = memberRepository.updateMemberName(member, memberDto.getName());

        return MemberInfoResponseDto.from(updatedMember);
    }
}
