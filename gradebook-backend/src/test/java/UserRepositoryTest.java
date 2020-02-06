import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pl.edu.agh.zarzecze.gradebook.Application;
import pl.edu.agh.zarzecze.gradebook.model.Course;
import pl.edu.agh.zarzecze.gradebook.model.Grade;
import pl.edu.agh.zarzecze.gradebook.model.Parent;
import pl.edu.agh.zarzecze.gradebook.model.User;
import pl.edu.agh.zarzecze.gradebook.repository.CourseRepository;
import pl.edu.agh.zarzecze.gradebook.repository.GradeRepository;
import pl.edu.agh.zarzecze.gradebook.repository.ParentRepository;
import pl.edu.agh.zarzecze.gradebook.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes=Application.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void Saving_ExampleUser_UserIsSavedProperly(){
        User userToAdd = new User("test@test.com",
                new BCryptPasswordEncoder().encode("test"),
                "test",
                "test");
        userRepository.save(userToAdd);

        boolean userAdded = false;

        for(User user : userRepository.findAll()) {
            userAdded |= user.getEmail().equals("test@test.com");
        }

        assertTrue(userAdded, "User wasn't added properly");
    }

    @Test
    public void FindByEmail_ExampleUser_UserIsFoundCorrectly(){
        String userEmail = "p.szczygi@zarz.ecze";
        User user = new User();
        user.setEmail(userEmail);
        userRepository.save(user);

        boolean userFound = userRepository.findByEmail(userEmail).isPresent();
        assertTrue(userFound, "User wasn't found correctly.");
    }
}