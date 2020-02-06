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
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<Grade> grades = new HashSet<>();

    @ManyToMany
    @JsonIgnore
    private Set<Group> groups = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "COURSE_FK")
    @JsonIgnore
    private Course course;

    private double gradeRating;

    private String name;
    
    public Assignment(String name) {
        this.name = name;
    }

    @PreRemove
    public void preRemove() {

    }

    public void onGroupRemoved(Group groupToDelete) {
        groups.remove(groupToDelete);
        grades.removeIf(grade ->
                groups.stream()
                        .flatMap(group -> group.getStudents().stream())
                        .distinct()
                        .noneMatch(student -> student.equals(grade.getStudent())));
    }

    public void onStudentRemoved(Student student) {
        grades.removeIf(grade -> grade.getStudent().equals(student));
    }
}