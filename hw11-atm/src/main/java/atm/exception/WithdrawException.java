package atm.exception;

public class WithdrawException extends RuntimeException {
    public WithdrawException(String message) {
        super(message);
    }
}
