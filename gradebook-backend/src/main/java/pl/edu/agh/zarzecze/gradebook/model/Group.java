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
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany
    private Set<Student> students = new HashSet<>();

    @ManyToMany(mappedBy = "groups")
    @JsonIgnore
    private Set<Course> courses = new HashSet<>();

    private String name;

    public Group(String name) {
        this.name = name;
    }

    @PreRemove
    public void preRemove() {
        courses.forEach(e -> e.onGroupRemoved(this));
        students.forEach(e -> e.getGroups().remove(this));
    }

    public void onStudentRemoved(Student student) {
        students.remove(student);
        courses.forEach(e -> e.onStudentRemoved(student));
    }
}
