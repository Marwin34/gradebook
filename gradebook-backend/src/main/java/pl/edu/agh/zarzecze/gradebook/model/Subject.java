package pl.edu.agh.zarzecze.gradebook.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private Set<Course> courses;

    private String name;
    
    public Subject(String name) {
        this.name = name;
    }

    public void addCourse(Course course) {
        courses.add(course);
    }
}
