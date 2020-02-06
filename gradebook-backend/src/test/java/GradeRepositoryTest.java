import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pl.edu.agh.zarzecze.gradebook.Application;
import pl.edu.agh.zarzecze.gradebook.model.Grade;
import pl.edu.agh.zarzecze.gradebook.model.Parent;
import pl.edu.agh.zarzecze.gradebook.repository.GradeRepository;
import pl.edu.agh.zarzecze.gradebook.repository.ParentRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes=Application.class)
@DataJpaTest
public class GradeRepositoryTest {

    @Autowired
    private GradeRepository gradeRepository;

    @Test
    public void Saving_ExampleGrade_GradeIsSavedProperly(){
        Grade gradeToAdd = new Grade();
        gradeRepository.save(gradeToAdd);

        boolean gradeAdded = gradeRepository.findAll().iterator().hasNext();

        assertTrue(gradeAdded, "Grade wasn't added properly");
    }
}