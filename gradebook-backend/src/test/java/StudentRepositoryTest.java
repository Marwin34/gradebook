import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pl.edu.agh.zarzecze.gradebook.Application;
import pl.edu.agh.zarzecze.gradebook.model.Student;
import pl.edu.agh.zarzecze.gradebook.repository.StudentRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes=Application.class)
@DataJpaTest
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void Saving_ExampleStudent_StudentIsSavedProperly(){
        Student studentToAdd = new Student("login", "password", "Jan", "Paweł");
        studentRepository.save(studentToAdd);

        boolean studentAdded = false;
        for(Student student : studentRepository.findAll()) {
            studentAdded |= student.getFirstName().equals("Jan");
        }

        assertTrue(studentAdded, "Student wasn't added properly");
    }

    @Test
    public void FindingByEmail_ExampleStudentIsFoundCorrectly(){
        String studentEmail = "p.szczygi@agh.pl";
        Student student = new Student();
        student.setEmail(studentEmail);

        studentRepository.save(student);

        boolean studentFound = studentRepository.findByEmail(studentEmail).isPresent();

        assertTrue(studentFound, "Student wasn't found.");
    }
}