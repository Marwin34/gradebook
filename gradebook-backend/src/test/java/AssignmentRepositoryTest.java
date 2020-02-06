import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pl.edu.agh.zarzecze.gradebook.Application;
import pl.edu.agh.zarzecze.gradebook.model.Assignment;
import pl.edu.agh.zarzecze.gradebook.repository.AssignmentRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes= Application.class)
@DataJpaTest
public class AssignmentRepositoryTest {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Test
    public void Saving_ExampleAssignment_AssignmentIsSavedProperly(){
        Assignment assignment = new Assignment();

        assignmentRepository.save(assignment);

        boolean assignmentAdded = assignmentRepository.findAll().iterator().hasNext();
        assertTrue(assignmentAdded, "Assignment wasn't added properly.");
    }
}
