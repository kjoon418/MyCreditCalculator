package junwatson.mycreditcalculator.service;

import junwatson.mycreditcalculator.domain.Member;
import junwatson.mycreditcalculator.dto.request.ManagerSignUpRequestDto;
import junwatson.mycreditcalculator.dto.request.MemberSignInRequestDto;
import junwatson.mycreditcalculator.dto.request.MemberSignUpRequestDto;
import junwatson.mycreditcalculator.dto.token.TokenDto;
import junwatson.mycreditcalculator.jwt.TokenProvider;
import junwatson.mycreditcalculator.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository repository;
    private final TokenProvider tokenProvider;

    @Transactional
    public TokenDto signUp(MemberSignUpRequestDto memberSignUpRequestDto) {
        Member member = repository.signUp(memberSignUpRequestDto);
        String accessToken = tokenProvider.createAccessToken(member);

        return TokenDto.builder()
                .accessToken(accessToken)
                .build();
    }

    @Transactional(readOnly = true)
    public TokenDto signIn(MemberSignInRequestDto memberSignInRequestDto) {
         return repository.signIn(memberSignInRequestDto);
    }
}
