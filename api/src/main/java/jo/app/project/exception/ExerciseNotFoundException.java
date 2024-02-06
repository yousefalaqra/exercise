package jo.app.project.exception;

public class ExerciseNotFoundException extends RuntimeException {
    public ExerciseNotFoundException(String code) {
        super(code);
    }
}