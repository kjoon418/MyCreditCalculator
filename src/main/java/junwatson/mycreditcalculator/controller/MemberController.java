package junwatson.mycreditcalculator.controller;

import junwatson.mycreditcalculator.dto.request.MemberUpdateEmailRequestDto;
import junwatson.mycreditcalculator.dto.request.MemberUpdateNameRequestDto;
import junwatson.mycreditcalculator.dto.request.MemberUpdatePasswordRequestDto;
import junwatson.mycreditcalculator.dto.response.MemberInfoResponseDto;
import junwatson.mycreditcalculator.exception.member.IllegalMemberStateException;
import junwatson.mycreditcalculator.exception.member.MemberNotExistException;
import junwatson.mycreditcalculator.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.PropertyValueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.SQLIntegrityConstraintViolationException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PatchMapping("/email")
    public ResponseEntity<MemberInfoResponseDto> updateEmail(@RequestBody MemberUpdateEmailRequestDto requestDto, Principal principal) {
        log.info("MemberController.updateEmail() called");

        Long memberId = Long.parseLong(principal.getName());
        MemberInfoResponseDto responseDto = memberService.updateMemberEmail(memberId, requestDto);

        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/password")
    public ResponseEntity<MemberInfoResponseDto> updatePassword(@RequestBody MemberUpdatePasswordRequestDto requestDto, Principal principal) {
        log.info("MemberController.updatePassword() called");

        Long memberId = Long.parseLong(principal.getName());
        MemberInfoResponseDto responseDto = memberService.updateMemberPassword(memberId, requestDto);

        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/name")
    public ResponseEntity<MemberInfoResponseDto> updateName(@RequestBody MemberUpdateNameRequestDto requestDto, Principal principal) {
        log.info("MemberController.updateName() called");

        Long memberId = Long.parseLong(principal.getName());
        MemberInfoResponseDto responseDto = memberService.updateMemberName(memberId, requestDto);

        return ResponseEntity.ok(responseDto);
    }


    @ExceptionHandler(MemberNotExistException.class)
    public ResponseEntity<String> handleMemberNotExistException(MemberNotExistException exception) {
        return ResponseEntity.status(NOT_FOUND).body(exception.getMessage());
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
