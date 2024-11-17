package junwatson.mycreditcalculator.controller;

import jakarta.servlet.http.HttpServletRequest;
import junwatson.mycreditcalculator.dto.request.EmailValidityCheckRequestDto;
import junwatson.mycreditcalculator.dto.request.MemberSignInRequestDto;
import junwatson.mycreditcalculator.dto.request.MemberSignUpRequestDto;
import junwatson.mycreditcalculator.dto.response.EmailValidityCheckResponseDto;
import junwatson.mycreditcalculator.dto.response.CreateTokenResponseDto;
import junwatson.mycreditcalculator.dto.response.ExpireTokenResponseDto;
import junwatson.mycreditcalculator.dto.response.ReissueTokenResponseDto;
import junwatson.mycreditcalculator.exception.member.IllegalMemberStateException;
import junwatson.mycreditcalculator.exception.token.IllegalTokenException;
import junwatson.mycreditcalculator.exception.member.MemberNotExistException;
import junwatson.mycreditcalculator.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.PropertyValueException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.SQLIntegrityConstraintViolationException;

import static org.springframework.http.HttpStatus.*;

/**
 * 토큰 발급과 관련된 API 및 인증이 필요 없는 API를 처리하는 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthorizationController {

    private final MemberService memberService;

    @PostMapping("/authorization")
    public ResponseEntity<CreateTokenResponseDto> signUp(@RequestBody MemberSignUpRequestDto requestDto) {
        log.info("AuthorizationController.signUp() called");

        CreateTokenResponseDto createTokenResponseDto = memberService.signUp(requestDto);

        return ResponseEntity.status(CREATED).body(createTokenResponseDto);
    }

    @GetMapping("/authorization")
    public ResponseEntity<CreateTokenResponseDto> signIn(@RequestBody MemberSignInRequestDto requestDto) {
        log.info("AuthorizationController.signIn() called");

        CreateTokenResponseDto createTokenResponseDto = memberService.signIn(requestDto);

        return ResponseEntity.ok(createTokenResponseDto);
    }

    @GetMapping("/authorization/reissue")
    public ResponseEntity<ReissueTokenResponseDto> reissueAccessToken(HttpServletRequest request) {
        log.info("AuthorizationController.reissueAccessToken() called");

        ReissueTokenResponseDto responseDto = memberService.reissueAccessToken(request);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/authorization/email")
    public ResponseEntity<EmailValidityCheckResponseDto> emailValidityCheck(@RequestBody EmailValidityCheckRequestDto requestDto) {
        log.info("MemberController.emailExistsCheck() called");

        EmailValidityCheckResponseDto responseDto = memberService.emailValidityCheck(requestDto);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/expire")
    public ResponseEntity<ExpireTokenResponseDto> logout(Principal principal) {
        log.info("AuthorizationController.logout() called");

        Long memberId = Long.parseLong(principal.getName());
        ExpireTokenResponseDto responseDto = memberService.expireTokens(memberId);

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<ExpireTokenResponseDto> withdraw(Principal principal) {
        log.info("AuthorizationController.withdraw() called");

        long memberId = Long.parseLong(principal.getName());
        ExpireTokenResponseDto responseDto = memberService.deleteMemberById(memberId);

        return ResponseEntity.ok(responseDto);
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

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<String> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException exception) {
        return ResponseEntity.status(BAD_REQUEST).body("다른 회원이 이미 사용하고 있는 정보입니다.");
    }
}
