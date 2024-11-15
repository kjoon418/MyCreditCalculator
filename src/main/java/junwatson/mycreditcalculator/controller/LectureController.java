package junwatson.mycreditcalculator.controller;

import junwatson.mycreditcalculator.dto.request.LectureRegistrationRequestDto;
import junwatson.mycreditcalculator.dto.request.LectureSearchRequestDto;
import junwatson.mycreditcalculator.dto.request.LectureUpdateRequestDto;
import junwatson.mycreditcalculator.dto.response.LectureInfoResponseDto;
import junwatson.mycreditcalculator.exception.lecture.IllegalLectureTypeException;
import junwatson.mycreditcalculator.exception.lecture.LectureNotExistException;
import junwatson.mycreditcalculator.exception.member.MemberNotExistException;
import junwatson.mycreditcalculator.service.LectureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.PropertyValueException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lecture")
@Slf4j
public class LectureController {

    private final LectureService lectureService;

    @GetMapping()
    public ResponseEntity<List<LectureInfoResponseDto>> findLectures(Principal principal) {
        log.info("LectureController.findLectures() called");

        long memberId = Long.parseLong(principal.getName());
        List<LectureInfoResponseDto> lectures = lectureService.findLectures(memberId);

        return ResponseEntity.ok(lectures);
    }

    @PostMapping()
    public ResponseEntity<LectureInfoResponseDto> registerLecture(@RequestBody LectureRegistrationRequestDto requestDto, Principal principal) {
        log.info("LectureController.registerLecture() called");

        long memberId = Long.parseLong(principal.getName());
        LectureInfoResponseDto responseDto = lectureService.registerLecture(memberId, requestDto);

        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping()
    public ResponseEntity<LectureInfoResponseDto> updateLecture(@RequestBody LectureUpdateRequestDto requestDto, Principal principal) {
        log.info("LectureController.updateLecture() called");

        Long memberId = Long.parseLong(principal.getName());
        LectureInfoResponseDto responseDto = lectureService.updateLecture(memberId, requestDto);

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<LectureInfoResponseDto> deleteLecture(@PathVariable Long id, Principal principal) {
        log.info("LectureController.deleteLecture() called");

        Long memberId = Long.parseLong(principal.getName());
        LectureInfoResponseDto responseDto = lectureService.deleteLecture(memberId, id);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/search")
    public ResponseEntity<List<LectureInfoResponseDto>> findLecturesByCondition(@RequestBody LectureSearchRequestDto requestDto, Principal principal) {
        log.info("LectureController.findLecturesByCondition() called");

        Long memberId = Long.parseLong(principal.getName());
        List<LectureInfoResponseDto> lectures = lectureService.searchLectures(memberId, requestDto);

        return ResponseEntity.ok(lectures);
    }

    @ExceptionHandler(MemberNotExistException.class)
    public ResponseEntity<String> handleMemberNotExistException(MemberNotExistException exception) {
        return ResponseEntity.status(NOT_FOUND).body(exception.getMessage());
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
