package pl.edu.agh.zarzecze.gradebook.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class Classes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "COURSE_FK")
    private Course course;

    @OneToOne
    @JoinColumn(name = "GROUP_FK")
    private Group group;

    private Date date;

    private String topic;
}
