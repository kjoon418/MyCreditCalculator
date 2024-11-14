package junwatson.mycreditcalculator.exception.token;

/**
 * 토큰 정보가 유효하지 않을 때 발생하는 예외
 */
public class IllegalTokenException extends RuntimeException {

    public IllegalTokenException(String message) {
        super(message);
    }

    public IllegalTokenException(Throwable throwable) {
        super(throwable);
    }
}
