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

import java.security.Principal;

@RestController
@RequestMapping("/credit")
@RequiredArgsConstructor
public class CreditController {

    private final CreditService creditService;

    @GetMapping
    public ResponseEntity<Double> getTotalAverageCredit(Principal principal) {
        long memberId = Long.parseLong(principal.getName());
        Double averageCredit = creditService.calculateTotalCredit(memberId);

        return ResponseEntity.ok(averageCredit);
    }

    @GetMapping("/search")
    public ResponseEntity<Double> getAverageCreditWithCondition(@RequestBody LectureSearchRequestDto requestDto, Principal principal) {
        long memberId = Long.parseLong(principal.getName());
        Double averageCredit = creditService.calculateCreditWithCondition(memberId, requestDto);

        return ResponseEntity.ok(averageCredit);
    }
}
