package pl.edu.agh.zarzecze.gradebook.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.zarzecze.gradebook.model.Subject;

import java.util.Optional;


@Repository
public interface SubjectRepository extends CrudRepository<Subject, Long> {
    Optional<Subject> findByName(String name);
}
