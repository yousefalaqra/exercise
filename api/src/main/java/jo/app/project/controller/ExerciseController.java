package jo.app.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jo.app.project.model.Exercise;
import jo.app.project.service.ExerciseService;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;

    @Autowired
    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<Exercise>> uploadExercises(@RequestParam("file") MultipartFile file) {
        List<Exercise> exercises = exerciseService.parseAndSaveCsvData(file);
        return ResponseEntity.ok(exercises);
    }

    @GetMapping("/")
    public List<Exercise> getAllExercises() {
        return exerciseService.findAll();
    }

    @GetMapping("/{code}")
    public ResponseEntity<Exercise> getExerciseByCode(@PathVariable String code) {
        Exercise exercise = exerciseService.getExerciseByCode(code);
        return ResponseEntity.ok(exercise);
    }

    @DeleteMapping("/")
    public ResponseEntity<String> deleteAllExercises() {
        exerciseService.deleteAll();
        return ResponseEntity.ok("All data deleted");
    }

}
