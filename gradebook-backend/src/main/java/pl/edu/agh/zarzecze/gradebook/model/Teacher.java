package pl.edu.agh.zarzecze.gradebook.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;

import static pl.edu.agh.zarzecze.gradebook.model.User.Role.ROLE_TEACHER;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class Teacher extends User {

    @ManyToMany(mappedBy = "teachers", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<Course> courses;
    
    public Teacher(String login, String password, String firstname, String lastname) {
        super(login, password, firstname, lastname);
        getRoles().add(ROLE_TEACHER);
    }
}
