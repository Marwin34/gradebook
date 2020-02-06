import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pl.edu.agh.zarzecze.gradebook.Application;
import pl.edu.agh.zarzecze.gradebook.model.Student;
import pl.edu.agh.zarzecze.gradebook.model.Teacher;
import pl.edu.agh.zarzecze.gradebook.repository.StudentRepository;
import pl.edu.agh.zarzecze.gradebook.repository.TeacherRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class)
@DataJpaTest
public class TeacherRepositoryTest {

    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    public void Saving_ExampleTeacher_TeacherIsSavedProperly() {
        Teacher teacherToAdd = new Teacher("login", "password", "Jan", "Cena");
        teacherRepository.save(teacherToAdd);

        boolean teacherAdded = false;
        for (Teacher teacher : teacherRepository.findAll()) {
            teacherAdded |= teacher.getFirstName().equals("Jan");
        }

        assertTrue(teacherAdded, "Teacher wasn't added properly");
    }

    @Test
    public void FindingByEmail_ExampleTeacher_TeacherIsFoundCorrectly() {
        String teacherEmail = "p.szczygi@zarz.ecze";
        Teacher teacher = new Teacher();
        teacher.setEmail(teacherEmail);
        teacherRepository.save(teacher);

        boolean teacherFound = teacherRepository.findByEmail(teacherEmail).isPresent();
        assertTrue(teacherFound, "Teacher wasn't found.");
    }
}