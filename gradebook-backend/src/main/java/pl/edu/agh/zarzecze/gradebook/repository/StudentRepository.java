package pl.edu.agh.zarzecze.gradebook.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.zarzecze.gradebook.model.Grade;
import pl.edu.agh.zarzecze.gradebook.model.Student;
import pl.edu.agh.zarzecze.gradebook.model.Teacher;

import java.util.Optional;


@Repository
public interface StudentRepository extends CrudRepository<Student, Long> {
    Optional<Student> findByEmail(String email);
}