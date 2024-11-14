package junwatson.mycreditcalculator.exception.member;

/**
 * 회원이 조회되지 않을 때 발생하는 예외
 */
public class MemberNotExistException extends RuntimeException {

    public MemberNotExistException() {
        super();
    }

    public MemberNotExistException(Exception e) {
        super(e);
    }

    public MemberNotExistException(String s) {
        super(s);
    }
}
