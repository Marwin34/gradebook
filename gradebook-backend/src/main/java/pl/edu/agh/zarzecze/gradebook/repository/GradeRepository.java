package pl.edu.agh.zarzecze.gradebook.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.zarzecze.gradebook.model.Course;
import pl.edu.agh.zarzecze.gradebook.model.Grade;
import pl.edu.agh.zarzecze.gradebook.model.Student;

import java.util.Arrays;
import java.util.List;


@Repository
public interface GradeRepository extends CrudRepository<Grade, Long> {
    List<Grade> findByStudentContaining(Student student);
}