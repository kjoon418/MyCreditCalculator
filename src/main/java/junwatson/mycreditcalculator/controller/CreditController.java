package junwatson.mycreditcalculator.controller;

import junwatson.mycreditcalculator.dto.request.LectureSearchRequestDto;
import junwatson.mycreditcalculator.exception.lecture.IllegalLectureTypeException;
import junwatson.mycreditcalculator.exception.lecture.LectureNotExistException;
import junwatson.mycreditcalculator.service.CreditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.PropertyValueException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/credit")
@RequiredArgsConstructor
@Slf4j
public class CreditController {

    private final CreditService creditService;

    @GetMapping
    public ResponseEntity<Double> getTotalAverageCredit(Principal principal) {
        log.info("CreditController.getTotalAverageCredit() called");

        long memberId = Long.parseLong(principal.getName());
        Double averageCredit = creditService.calculateTotalCredit(memberId);

        return ResponseEntity.ok(averageCredit);
    }

    @GetMapping("/search")
    public ResponseEntity<Double> getAverageCreditWithCondition(@RequestBody LectureSearchRequestDto requestDto, Principal principal) {
        log.info("CreditController.getAverageCreditWithCondition() called");

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

    @ExceptionHandler(PropertyValueException.class)
    public ResponseEntity<String> handlePropertyValueException(PropertyValueException exception) {
        return ResponseEntity.status(BAD_REQUEST).body("잘못된 값 전달입니다.");
    }
}
