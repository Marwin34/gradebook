import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pl.edu.agh.zarzecze.gradebook.Application;
import pl.edu.agh.zarzecze.gradebook.model.Parent;
import pl.edu.agh.zarzecze.gradebook.repository.ParentRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes=Application.class)
@DataJpaTest
public class ParentRepositoryTest {

    @Autowired
    private ParentRepository parentRepository;

    @Test
    public void Saving_ExampleParent_ParentIsSavedProperly(){
        Parent parentToAdd = new Parent("login", "password", "Jan", "Cena");
        parentRepository.save(parentToAdd);

        boolean parentAdded = false;
        for(Parent parent : parentRepository.findAll()) {
            parentAdded |= parent.getFirstName().equals("Jan");
        }

        assertTrue(parentAdded, "Parent wasn't added properly");
    }
}