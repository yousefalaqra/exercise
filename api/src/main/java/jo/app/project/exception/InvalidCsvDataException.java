package jo.app.project.exception;

public class InvalidCsvDataException extends RuntimeException {
    public InvalidCsvDataException(String message) {
        super(message);
    }
}