import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.edu.agh.zarzecze.gradebook.Application;
import pl.edu.agh.zarzecze.gradebook.controllers.AdminTeacherController;
import pl.edu.agh.zarzecze.gradebook.model.Teacher;
import pl.edu.agh.zarzecze.gradebook.repository.TeacherRepository;
import pl.edu.agh.zarzecze.gradebook.services.PasswordService;

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
public class AdminTeacherControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    AdminTeacherController adminTeacherController;

    @Test
    @WithMockUser
    public void ReadingEndPoint_QueryTeachers_TeachersAreReturned() throws Exception {
        Teacher teacher = new Teacher("login", "password", "Jan", "Nowak");

        List<Teacher> allTeachers = Collections.singletonList(teacher);

        given(adminTeacherController.getAllTeachers()).willReturn(allTeachers);

        mvc.perform(MockMvcRequestBuilders.get("/api/teachers")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is(teacher.getFirstName())))
                .andExpect(jsonPath("$[0].lastName", is(teacher.getLastName())));
    }
}
