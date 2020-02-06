package pl.edu.agh.zarzecze.gradebook.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Presence {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;

   @ManyToOne
   @JoinColumn(name = "STUDENT_FK")
   private Student student;

   @OneToOne
   @JoinColumn(name = "CLASSES_FK")
   private Classes classes;

   private boolean present;
}
