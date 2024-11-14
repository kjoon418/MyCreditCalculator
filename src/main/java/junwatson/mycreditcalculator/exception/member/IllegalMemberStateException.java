package junwatson.mycreditcalculator.exception.member;

/**
 * 회원 생성 혹은 회원 수정시에, 적합하지 않은 정보를 전달하면 발생하는 예외
 */
public class IllegalMemberStateException extends RuntimeException {

    public IllegalMemberStateException () {
        super();
    }

    public IllegalMemberStateException (Exception e) {
        super(e);
    }

    public IllegalMemberStateException (String s) {
        super(s);
    }
}
