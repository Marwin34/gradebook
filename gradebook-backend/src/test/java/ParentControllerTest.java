import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.edu.agh.zarzecze.gradebook.Application;
import pl.edu.agh.zarzecze.gradebook.controllers.ParentController;
import pl.edu.agh.zarzecze.gradebook.model.Parent;
import pl.edu.agh.zarzecze.gradebook.repository.ParentRepository;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = Application.class)
@ContextConfiguration
public class ParentControllerTest {

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private MockMvc mvc;

    @MockBean
    ParentController parentController;

    @Test
    @WithMockUser
    public void ReadingEndpoint_QueryParents_ParentsAreReturned() throws Exception {
        Parent parent = new Parent("login", "password", "Jan", "Cena");

        List<Parent> allParents = Collections.singletonList(parent);

        given(parentController.getAllParents()).willReturn(allParents);

        mvc.perform(MockMvcRequestBuilders.get("/parents")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is(parent.getFirstName())))
                .andExpect(jsonPath("$[0].lastName", is(parent.getLastName())));
    }
}