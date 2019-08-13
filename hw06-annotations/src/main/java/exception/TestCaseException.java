package exception;

public class TestCaseException extends Exception {
    public TestCaseException(Exception e) {
        super(e.getMessage(), e.getCause());
    }
}
