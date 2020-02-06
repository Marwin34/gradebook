package pl.edu.agh.zarzecze.gradebook.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.zarzecze.gradebook.model.Teacher;

import java.util.Optional;


@Repository
public interface TeacherRepository extends CrudRepository<Teacher, Long> {
    Optional<Teacher> findByEmail(String email);
}
