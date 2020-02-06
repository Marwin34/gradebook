package pl.edu.agh.zarzecze.gradebook.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class Student extends User {
    @OneToMany(mappedBy = "student", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<Grade> grades = new HashSet<>();

    @ManyToMany(mappedBy = "students")
    @JsonIgnore
    private Set<Group> groups = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "PARENT_FK")
    @JsonIgnore
    private Parent parent;

    public Student(String login, String password, String firstname, String lastname) {
        super(login, password, firstname, lastname);
    }

    public void addGrade(Grade grade) {
        grades.add(grade);
    }

    @PreRemove
    public void preRemove() {
        groups.forEach(e -> e.onStudentRemoved(this));
    }
}
