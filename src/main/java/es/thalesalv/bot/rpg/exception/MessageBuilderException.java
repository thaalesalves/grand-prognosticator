package es.thalesalv.bot.rpg.exception;

public class MessageBuilderException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MessageBuilderException(String message) {
        super(message);
    }

    public MessageBuilderException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageBuilderException(Throwable cause) {
        super(cause);
    }
}