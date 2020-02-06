package pl.edu.agh.zarzecze.gradebook.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.zarzecze.gradebook.model.Assignment;
import pl.edu.agh.zarzecze.gradebook.model.Course;
import pl.edu.agh.zarzecze.gradebook.model.Group;
import pl.edu.agh.zarzecze.gradebook.model.Teacher;

import java.util.List;


@Repository
public interface AssignmentRepository extends CrudRepository<Assignment, Long> {
    List<Assignment> findAll();
}