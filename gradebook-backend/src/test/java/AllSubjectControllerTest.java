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
import pl.edu.agh.zarzecze.gradebook.controllers.AdminSubjectController;
import pl.edu.agh.zarzecze.gradebook.model.Subject;
import pl.edu.agh.zarzecze.gradebook.repository.SubjectRepository;

import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = Application.class)
@ContextConfiguration
public class AllSubjectControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    AdminSubjectController adminSubjectController;

    @Test
    @WithMockUser
    public void ReadingEndpoint_QuerySubjects_SubjectsAreReturned() throws Exception {
        Subject subject = new Subject("IT");

        Iterable<Subject> allSubjects =
                Stream.of(subject)
                        .collect(toList());

        given(adminSubjectController.getAllSubjects()).willReturn(allSubjects);

        mvc.perform(MockMvcRequestBuilders.get("/api/subjects")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(subject.getName())));
    }
}
