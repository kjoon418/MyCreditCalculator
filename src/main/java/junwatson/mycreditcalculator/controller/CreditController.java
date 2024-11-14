package junwatson.mycreditcalculator.controller;

import jakarta.servlet.http.HttpServletRequest;
import junwatson.mycreditcalculator.dto.request.LectureSearchRequestDto;
import junwatson.mycreditcalculator.exception.lecture.IllegalLectureTypeException;
import junwatson.mycreditcalculator.exception.lecture.LectureNotExistException;
import junwatson.mycreditcalculator.jwt.TokenProvider;
import junwatson.mycreditcalculator.service.CreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

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

    @ExceptionHandler(LectureNotExistException.class)
    public ResponseEntity<String> handleLectureNotExistException(LectureNotExistException exception) {
        return ResponseEntity.status(NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(IllegalLectureTypeException.class)
    public ResponseEntity<String> handIllegalLectureTypeException(IllegalLectureTypeException exception) {
        return ResponseEntity.status(BAD_REQUEST).body(exception.getMessage());
    }
}
