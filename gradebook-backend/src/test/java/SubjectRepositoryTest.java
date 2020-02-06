import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pl.edu.agh.zarzecze.gradebook.Application;
import pl.edu.agh.zarzecze.gradebook.model.Subject;
import pl.edu.agh.zarzecze.gradebook.repository.SubjectRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class)
@DataJpaTest
public class SubjectRepositoryTest {

    @Autowired
    private SubjectRepository subjectRepository;

    @Test
    public void Saving_ExampleSubject_SubjectIsSavedProperly(){
        Subject subject = new Subject();
        subjectRepository.save(subject);

        boolean subjectAdded = subjectRepository.findAll().iterator().hasNext();

        assertTrue(subjectAdded, "Subject wasn't added properly.");
    }

    @Test
    public void FindingByName_ExampleSubject_SubjectIsFoundCorrectly(){
        String subjectName = "Physics";
        Subject subject = new Subject(subjectName);
        subjectRepository.save(subject);

        Optional<Subject> result = subjectRepository.findByName(subjectName);
        assertTrue(result.isPresent(), "Subject wasn't found.");
    }
}
