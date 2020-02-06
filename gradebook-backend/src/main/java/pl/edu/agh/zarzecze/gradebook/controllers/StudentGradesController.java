package pl.edu.agh.zarzecze.gradebook.controllers;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.zarzecze.gradebook.model.*;
import pl.edu.agh.zarzecze.gradebook.repository.CourseRepository;
import pl.edu.agh.zarzecze.gradebook.repository.GradeRepository;
import pl.edu.agh.zarzecze.gradebook.repository.StudentRepository;
import pl.edu.agh.zarzecze.gradebook.repository.SubjectRepository;

import java.util.Set;

import static java.util.stream.Collectors.toList;


@RestController
public class StudentGradesController {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectRepository subjectRepository;
    
    @PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping("/api/student/courseAssignmentGrades")
    public Iterable<StudentCourseAssignmentGrade> getAllGrades() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        Student currentUser = studentRepository.findByEmail(currentUserEmail).get();

        return gradeRepository.findByStudentContaining(currentUser).stream()
                .map(StudentCourseAssignmentGrade::new)
                .collect(toList());
    }

    @Getter
    @Setter
    private static class StudentCourseAssignmentGrade {

        private final Grade grade;
        private final Assignment assignment;
        private final Course course;

        StudentCourseAssignmentGrade(Grade grade) {
            this.grade = grade;
            this.assignment = grade.getAssignment();
            this.course = grade.getAssignment().getCourse();
        }
    }
}
