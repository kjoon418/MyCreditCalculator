package junwatson.mycreditcalculator.controller;

import jakarta.servlet.http.HttpServletRequest;
import junwatson.mycreditcalculator.dto.request.LectureRegistrationRequestDto;
import junwatson.mycreditcalculator.dto.request.LectureSearchRequestDto;
import junwatson.mycreditcalculator.dto.response.LectureInfoResponseDto;
import junwatson.mycreditcalculator.jwt.TokenProvider;
import junwatson.mycreditcalculator.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lecture")
public class LectureController {

    private final LectureService lectureService;

    @GetMapping()
    public ResponseEntity<List<LectureInfoResponseDto>> findLectures(Principal principal) {
        long memberId = Long.parseLong(principal.getName());
        List<LectureInfoResponseDto> lectures = lectureService.findLectures(memberId);

        return ResponseEntity.ok(lectures);
    }

    @PostMapping()
    public ResponseEntity<LectureInfoResponseDto> registerLecture(@RequestBody LectureRegistrationRequestDto requestDto, Principal principal) {
        long memberId = Long.parseLong(principal.getName());
        LectureInfoResponseDto responseDto = lectureService.registerLecture(memberId, requestDto);

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<LectureInfoResponseDto> deleteLecture(@PathVariable Long id, Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
        LectureInfoResponseDto responseDto = lectureService.deleteLecture(memberId, id);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/search")
    public ResponseEntity<List<LectureInfoResponseDto>> findLecturesByCondition(@RequestBody LectureSearchRequestDto requestDto, Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
        List<LectureInfoResponseDto> lectures = lectureService.searchLectures(memberId, requestDto);

        return ResponseEntity.ok(lectures);
    }

}
