package jo.app.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jo.app.project.model.Exercise;


public interface ExerciseRepository extends JpaRepository<Exercise, String> {

}
