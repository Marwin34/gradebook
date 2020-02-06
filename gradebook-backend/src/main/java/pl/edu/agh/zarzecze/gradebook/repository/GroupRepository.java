package pl.edu.agh.zarzecze.gradebook.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.zarzecze.gradebook.model.Group;

import java.util.List;


@Repository
public interface GroupRepository extends CrudRepository<Group, Long> {
    List<Group> findAll();
}