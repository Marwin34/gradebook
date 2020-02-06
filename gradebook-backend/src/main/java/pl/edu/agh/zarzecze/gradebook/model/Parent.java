package pl.edu.agh.zarzecze.gradebook.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class Parent extends User {
    @OneToMany(mappedBy = "parent")
    private Set<Student> students;
    
    public Parent(String login, String password, String firstname, String lastname) {
        super(login, password, firstname, lastname);
    }
}
