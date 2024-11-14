package junwatson.mycreditcalculator.controller;

import jakarta.servlet.http.HttpServletRequest;
import junwatson.mycreditcalculator.dto.request.LectureSearchRequestDto;
import junwatson.mycreditcalculator.jwt.TokenProvider;
import junwatson.mycreditcalculator.service.CreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/credit")
@RequiredArgsConstructor
public class CreditController {

    private final CreditService creditService;
    private final TokenProvider tokenProvider;

    @GetMapping
    public ResponseEntity<Double> getTotalAverageCredit(HttpServletRequest request) {
        String token = tokenProvider.resolveToken(request);
        Double averageCredit = creditService.calculateTotalCredit(token);

        return ResponseEntity.ok(averageCredit);
    }

    @GetMapping("/search")
    public ResponseEntity<Double> getAverageCreditWithCondition(@RequestBody LectureSearchRequestDto requestDto, HttpServletRequest request) {
        String token = tokenProvider.resolveToken(request);
        Double averageCredit = creditService.calculateCreditWithCondition(token, requestDto);

        return ResponseEntity.ok(averageCredit);
    }
}
