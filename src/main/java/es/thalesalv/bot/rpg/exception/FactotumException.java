package es.thalesalv.bot.rpg.exception;

public class FactotumException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FactotumException() {
        super();
    }

    public FactotumException(Throwable t) {
        super(t);
    }

    public FactotumException(String message) {
        super(message);
    }

    public FactotumException(String message, Throwable t) {
        super(message, t);
    }
}