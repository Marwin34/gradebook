package pl.edu.agh.zarzecze.gradebook.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private GradeValue gradeValue;

    @ManyToOne
    @JoinColumn(name = "STUDENT_FK")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "ASSIGNMENT_FK")
    private Assignment assignment;
    
    public Grade(GradeValue gradeValue, Student student, Assignment assignment) {
        this.gradeValue = gradeValue;
        this.student = student;
        this.assignment = assignment;
    }

    @PreRemove
    public void preRemove() {
        assignment.getGrades().remove(this);
    }
    
    public enum GradeValue {
        MINUS_ONE("-1"),
        ONE("1"),
        PLUS_ONE("+1"),
        MINUS_TWO("-2"),
        TWO("2"),
        PLUS_TWO("+2"),
        MINUS_THREE("-3"),
        THREE("3"),
        PLUS_THREE("+3"),
        MINUS_FOUR("-4"),
        FOUR("4"),
        PLUS_FOUR("+4"),
        MINUS_FIVE("-5"),
        FIVE("5"),
        PLUS_FIVE("+5"),
        MINUS_SIX("-6"),
        SIX("6"),
        PLUS_SIX("+6");

        public final String val;

        
        GradeValue(String val) {
            this.val = val;
        }

        public static GradeValue of(String gradeValueText) {
            for (GradeValue gradeValue : GradeValue.values()) {
                if (gradeValue.getValue().equals(gradeValueText)) {
                    return gradeValue;
                }
            }
            return null;
        }

        public String getValue() {
            return val;
        }
    }
}