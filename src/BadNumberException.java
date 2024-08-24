
public class BadNumberException extends RuntimeException {

    public BadNumberException() {
    }

    public BadNumberException(String message) {
        super(message);
    }

    public BadNumberException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadNumberException(Throwable cause) {
        super(cause);
    }

    public BadNumberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
