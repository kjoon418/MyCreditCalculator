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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lecture")
public class LectureController {

    private final TokenProvider tokenProvider;
    private final LectureService lectureService;

    @GetMapping()
    public ResponseEntity<List<LectureInfoResponseDto>> findLectures(HttpServletRequest request) {
        String token = tokenProvider.resolveToken(request);
        List<LectureInfoResponseDto> lectures = lectureService.findLectures(token);

        return ResponseEntity.ok(lectures);
    }

    @PostMapping()
    public ResponseEntity<LectureInfoResponseDto> registerLecture(@RequestBody LectureRegistrationRequestDto requestDto, HttpServletRequest request) {
        String token = tokenProvider.resolveToken(request);
        LectureInfoResponseDto responseDto = lectureService.registerLecture(token, requestDto);

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<LectureInfoResponseDto> deleteLecture(@PathVariable Long id, HttpServletRequest request) {
        String token = tokenProvider.resolveToken(request);
        LectureInfoResponseDto responseDto = lectureService.deleteLecture(token, id);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/search")
    public ResponseEntity<List<LectureInfoResponseDto>> findLecturesByCondition(@RequestBody LectureSearchRequestDto requestDto, HttpServletRequest request) {
        String token = tokenProvider.resolveToken(request);
        List<LectureInfoResponseDto> lectures = lectureService.searchLectures(token, requestDto);

        return ResponseEntity.ok(lectures);
    }

}
