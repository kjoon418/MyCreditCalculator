package junwatson.mycreditcalculator.exception;

/**
 * LectureType에 대한 입력이 부적절하면 발생하는 예외
 */
public class IllegalLectureTypeException extends RuntimeException {

    public IllegalLectureTypeException(Throwable e) {
        super(e);
    }

    public IllegalLectureTypeException(String message) {
        super(message);
    }
}
