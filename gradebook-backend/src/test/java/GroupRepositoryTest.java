import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pl.edu.agh.zarzecze.gradebook.Application;
import pl.edu.agh.zarzecze.gradebook.model.Group;
import pl.edu.agh.zarzecze.gradebook.repository.GroupRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes= Application.class)
@DataJpaTest
public class GroupRepositoryTest {

    @Autowired
    private GroupRepository groupRepository;

    @Test
    public void Saving_ExampleGroup_GroupIsSavedProperly(){
        Group group = new Group();
        groupRepository.save(group);

        boolean groupAdded = groupRepository.findAll().iterator().hasNext();

        assertTrue(groupAdded, "Group wasn't added properly.");
    }
}
