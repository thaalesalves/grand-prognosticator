package es.thalesalv.bot.rpg.exception;

public class WatsonException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public WatsonException(String message) {
        super(message);
    }

    public WatsonException(String message, Throwable cause) {
        super(message, cause);
    }

    public WatsonException(Throwable cause) {
        super(cause);
    }
}