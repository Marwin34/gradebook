package pl.edu.agh.zarzecze.gradebook.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
    private List<Assignment> assignments = new ArrayList<>();

    @OneToMany(mappedBy = "course")
    private Set<Classes> classes = new HashSet<>();

    @ManyToMany
    private Set<Teacher> teachers = new HashSet<>();

    @ManyToMany
    private Set<Group> groups = new HashSet<>();

    @ManyToOne()
    @JoinColumn(name = "SUBJECT_FK")
    @JsonBackReference
    private Subject subject;

    private String name;

    public Course(String name) {
        this.name = name;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    @PreRemove
    public void preRemove() {
        teachers.forEach(e -> e.getCourses().remove(this));
        groups.forEach(e -> e.getCourses().remove(this));
    }

    public void onGroupRemoved(Group group) {
        assignments.forEach(e -> e.onGroupRemoved(group));
        groups.remove(group);
    }

    public void onStudentRemoved(Student student) {
        assignments.forEach(e -> e.onStudentRemoved(student));
    }
}
