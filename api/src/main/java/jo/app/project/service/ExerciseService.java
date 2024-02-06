package jo.app.project.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import jo.app.project.exception.ExerciseNotFoundException;
import jo.app.project.exception.FileSizeExceededException;
import jo.app.project.exception.InvalidCsvDataException;
import jo.app.project.exception.InvalidFileTypeException;
import jo.app.project.exception.InvalidFilenameException;
import jo.app.project.model.Exercise;
import jo.app.project.repository.ExerciseRepository;

@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final int maxFileSize = 5000;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public List<Exercise> parseAndSaveCsvData(MultipartFile file)
            throws InvalidCsvDataException, InvalidFileTypeException, FileSizeExceededException,
            InvalidFilenameException {
        this.validateFile(file); // Validate first

        List<Exercise> exercises = parseCsvData(file); // Separate parsing logic
        return exerciseRepository.saveAll(exercises);
    }

    public List<Exercise> findAll() {
        return this.exerciseRepository.findAll();
    }

    public Exercise getExerciseByCode(String code) {
        return exerciseRepository.findById(code)
                .orElseThrow(() -> new ExerciseNotFoundException(code));
    }

    public void deleteAll() {
        this.exerciseRepository.deleteAll();
    }

    private List<Exercise> parseCsvData(MultipartFile file) throws InvalidCsvDataException {

        List<Exercise> exercises = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            List<String[]> csvRows = csvReader.readAll();

            // Skip headers
            for (int i = 1; i < csvRows.size(); i++) {
                Exercise exercise = mapCsvRowToExercise(csvRows.get(i));
                exercises.add(exercise);
            }
        } catch (IOException | CsvException e) {
            throw new InvalidCsvDataException("Failed to parse CSV file");
        }

        return exercises;
    }

    private Exercise mapCsvRowToExercise(String[] record) {
        Exercise exercise = new Exercise();
        exercise.setSource(record[0]);
        exercise.setCodeListCode(record[1]);

        exercise.setCode(record[2]);
        exercise.setDisplayValue(record[3]);
        exercise.setLongDescription(record[4]);
        exercise.setFromDate(record[5]);
        exercise.setToDate(record[6]);

        String sortingPriorityStr = record[7];
        int sortingPriority = Optional.ofNullable(sortingPriorityStr)
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .orElse(0); // Default if null or empty
        exercise.setSortingPriority(sortingPriority);

        return exercise;
    }

    private void validateFile(MultipartFile file)
            throws InvalidFileTypeException, FileSizeExceededException, InvalidFilenameException {
        if (!file.getContentType().equals("text/csv")) {
            throw new InvalidFileTypeException("Only CSV files are allowed");
        }

        System.err.println(file.getSize());
        if (file.getSize() > maxFileSize) {
            throw new FileSizeExceededException("File size exceeds maximum limit");
        }

        sanitizeFilename(file.getName());

    }

    private String sanitizeFilename(String filename) {
        String regex = "^[a-zA-Z0-9_.-]+$";
        if (!filename.matches(regex)) {
            throw new InvalidFilenameException("Filename contains invalid characters");
        }
        // Additional filtering or encoding if needed
        return filename;
    }

}
