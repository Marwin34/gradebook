package pl.edu.agh.zarzecze.gradebook.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.zarzecze.gradebook.model.Parent;


@Repository
public interface ParentRepository extends CrudRepository<Parent, Long> {
}
