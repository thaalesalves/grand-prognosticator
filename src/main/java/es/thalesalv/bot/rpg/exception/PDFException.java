package es.thalesalv.bot.rpg.exception;

public class PDFException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PDFException(String message) {
        super(message);
    }

    public PDFException(String message, Throwable cause) {
        super(message, cause);
    }

    public PDFException(Throwable cause) {
        super(cause);
    }
}