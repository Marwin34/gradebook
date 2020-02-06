import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pl.edu.agh.zarzecze.gradebook.Application;
import pl.edu.agh.zarzecze.gradebook.model.Course;
import pl.edu.agh.zarzecze.gradebook.model.Grade;
import pl.edu.agh.zarzecze.gradebook.model.Parent;
import pl.edu.agh.zarzecze.gradebook.repository.CourseRepository;
import pl.edu.agh.zarzecze.gradebook.repository.GradeRepository;
import pl.edu.agh.zarzecze.gradebook.repository.ParentRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes=Application.class)
@DataJpaTest
public class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    public void Saving_ExampleCourse_CourseIsSavedProperly(){
        Course courseToAdd = new Course();
        courseRepository.save(courseToAdd);

        boolean courseAdded = courseRepository.findAll().iterator().hasNext();

        assertTrue(courseAdded, "Course wasn't added properly");
    }
}