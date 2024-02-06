package jo.app.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jo.app.project.model.ApiError;

@ControllerAdvice
public class ExceptionMapper {

    @ExceptionHandler(InvalidFilenameException.class)
    public ResponseEntity<ApiError> handleInvalidFilename(InvalidFilenameException e) {
        return ResponseEntity.badRequest()
                .body(new ApiError("Invalid file name. Please use a valid file name for CSV files.",
                        HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(InvalidFileTypeException.class)
    public ResponseEntity<ApiError> handleInvalidFileType(InvalidFileTypeException e) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(new ApiError("Only CSV files are supported. Please upload a CSV file.",
                        HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()));
    }

    @ExceptionHandler(FileSizeExceededException.class)
    public ResponseEntity<ApiError> handleFileSizeExceededException(FileSizeExceededException e) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(new ApiError("File size exceeds the maximum allowed limit. Please upload a smaller file.",
                        HttpStatus.PAYLOAD_TOO_LARGE.value()));
    }

    @ExceptionHandler(InvalidCsvDataException.class)
    public ResponseEntity<ApiError> handleInvalidCsvData(InvalidCsvDataException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                new ApiError("The CSV file contains invalid or corrupt data. Please check the file and try again.",
                        HttpStatus.UNPROCESSABLE_ENTITY.value()));
    }

    @ExceptionHandler(ExerciseNotFoundException.class)
    public ResponseEntity<ApiError> handleExerciseNotFound(ExerciseNotFoundException code) {

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                new ApiError("Exercise with code " + code + " not found.", HttpStatus.NOT_FOUND.value()));
    }
}
