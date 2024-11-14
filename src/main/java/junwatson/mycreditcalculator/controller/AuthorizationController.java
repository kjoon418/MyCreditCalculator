package junwatson.mycreditcalculator.controller;

import junwatson.mycreditcalculator.domain.Member;
import junwatson.mycreditcalculator.dto.request.MemberSignInRequestDto;
import junwatson.mycreditcalculator.dto.request.MemberSignUpRequestDto;
import junwatson.mycreditcalculator.dto.token.TokenDto;
import junwatson.mycreditcalculator.exception.member.IllegalMemberStateException;
import junwatson.mycreditcalculator.exception.token.IllegalTokenException;
import junwatson.mycreditcalculator.exception.member.MemberNotExistException;
import junwatson.mycreditcalculator.jwt.TokenProvider;
import junwatson.mycreditcalculator.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.hibernate.PropertyValueException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static org.springframework.http.HttpStatus.*;

/**
 * 토큰 발급과 관련된 API를 처리하는 메서드
 */
@RestController
@RequiredArgsConstructor
public class AuthorizationController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @PostMapping("/authorization")
    public ResponseEntity<TokenDto> signUp(@RequestBody MemberSignUpRequestDto memberDto) {
        TokenDto tokenDto = memberService.signUp(memberDto);

        return ResponseEntity.status(CREATED).body(tokenDto);
    }

    @GetMapping("/authorization")
    public ResponseEntity<TokenDto> signIn(@RequestBody MemberSignInRequestDto memberDto) {
        TokenDto tokenDto = memberService.signIn(memberDto);

        return ResponseEntity.ok(tokenDto);
    }

    @GetMapping("/expire")
    public ResponseEntity<TokenDto> logout(Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
        Member member = memberService.findMemberById(memberId);
        String token = tokenProvider.expireAccessToken(member);
        TokenDto tokenDto = TokenDto.builder()
                .accessToken(token)
                .build();

        return ResponseEntity.ok(tokenDto);
    }

    @ExceptionHandler(IllegalTokenException.class)
    public ResponseEntity<String> handleIllegalTokenException(IllegalTokenException exception) {
        return ResponseEntity.status(FORBIDDEN).body(exception.getMessage());
    }

    @ExceptionHandler(MemberNotExistException.class)
    public ResponseEntity<String> handleMemberNotExistException(MemberNotExistException exception) {
        return ResponseEntity.status(BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(IllegalMemberStateException.class)
    public ResponseEntity<String> handleIllegalMemberStateException(IllegalMemberStateException exception) {
        return ResponseEntity.status(BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(PropertyValueException.class)
    public ResponseEntity<String> handlePropertyValueException(PropertyValueException exception) {
        return ResponseEntity.status(BAD_REQUEST).body("잘못된 값 전달입니다.");
    }
}
