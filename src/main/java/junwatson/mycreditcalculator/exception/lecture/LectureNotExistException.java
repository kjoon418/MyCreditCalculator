package junwatson.mycreditcalculator.exception.lecture;

/**
 * 검색 결과로 강의가 없을 때 발생하는 예외
 */
public class LectureNotExistException extends RuntimeException {

    public LectureNotExistException(String message) {
        super(message);
    }

    public LectureNotExistException(Throwable t) {
        super(t);
    }
}
