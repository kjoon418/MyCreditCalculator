package junwatson.mycreditcalculator.controller;

import junwatson.mycreditcalculator.dto.request.MemberSignInRequestDto;
import junwatson.mycreditcalculator.dto.request.MemberSignUpRequestDto;
import junwatson.mycreditcalculator.dto.token.TokenDto;
import junwatson.mycreditcalculator.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authorization")
@RequiredArgsConstructor
public class TokenController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<TokenDto> signUp(@RequestBody MemberSignUpRequestDto memberDto) {
        TokenDto tokenDto = memberService.signUp(memberDto);

        return ResponseEntity.ok(tokenDto);
    }

    @GetMapping
    public ResponseEntity<TokenDto> signIn(@RequestBody MemberSignInRequestDto memberDto) {
        TokenDto tokenDto = memberService.signIn(memberDto);

        return ResponseEntity.ok(tokenDto);
    }

}
