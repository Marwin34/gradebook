package pl.edu.agh.zarzecze.gradebook.controllers;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.agh.zarzecze.gradebook.model.*;
import pl.edu.agh.zarzecze.gradebook.repository.*;

import java.net.URI;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.FORBIDDEN;


@RestController
public class TeacherAssignmentGradesController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Data
    private static class AddGradeToAssignmentRequest {
        private Long studentId;
        private String gradeValue;
    }

    @PreAuthorize("hasAnyRole('TEACHER')")
    @GetMapping("/api/teacher/courses/{courseId}/assignments/{assignmentId}/grades")
    public Set<Grade> getAssignmentGrades(@PathVariable(value = "courseId") Long id,
                                          @PathVariable(value = "assignmentId") Long assignmentId) {
        return assignmentRepository.findById(assignmentId).get().getGrades();
    }

    @PreAuthorize("hasAnyRole('TEACHER')")
    @PostMapping("/api/teacher/courses/{courseId}/assignments/{assignmentId}/grades")
    public ResponseEntity<?> addGradeToAssignment(@PathVariable(value = "courseId") Long courseId,
                                                  @PathVariable(value = "assignmentId") Long assignmentId,
                                                  @RequestBody AddGradeToAssignmentRequest addGradeToAssignmentRequest) {

        Course course = courseRepository.findById(courseId).get();
        Assignment assignment = assignmentRepository.findById(assignmentId).get();
        Student student = studentRepository.findById(addGradeToAssignmentRequest.getStudentId()).get();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Teacher currentUser = teacherRepository.findByEmail(authentication.getName()).get();

        if (!course.getTeachers().contains(currentUser)) {
            return ResponseEntity.status(FORBIDDEN).body(
                    new ErrorResponse("TEACHER_DOESNT_HAVE_PRIVILEGES_TO_THIS_COURSE",
                            "This teacher cannot modify course."));
        }

        if (assignment.getGroups().stream()
                .flatMap(e -> e.getStudents().stream())
                .noneMatch(e -> e.equals(student))) {
            return ResponseEntity.status(FORBIDDEN).body(
                    new ErrorResponse("STUDENT_NOT_IN_THIS_ASSIGNMENT",
                            "This student isn't in this assignment."));
        }

        if (assignment.getGrades().stream()
                .anyMatch(e -> e.getStudent().getId() == addGradeToAssignmentRequest.studentId)) {
            return ResponseEntity.status(FORBIDDEN).body(
                    new ErrorResponse("STUDENT_ALREADY_HAS_GRADE_IN_ASSIGNMENT",
                            "This student already has grade in this assignment."));
        }

        Grade newGrade = new Grade(Grade.GradeValue.of(addGradeToAssignmentRequest.gradeValue), student, assignment);
        assignment.getGrades().add(newGrade);

        gradeRepository.save(newGrade);
        assignmentRepository.save(assignment);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newGrade.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('TEACHER')")
    @DeleteMapping("/api/teacher/courses/{courseId}/assignments/{assignmentId}/grades/{gradeId}")
    public ResponseEntity<?> removeGradeFromAssignment(@PathVariable(value = "courseId") Long id,
                                                       @PathVariable(value = "assignmentId") Long assignmentId,
                                                       @PathVariable(value = "gradeId") Long gradeId) {

        Assignment assignment = assignmentRepository.findById(assignmentId).get();
        Grade grade = gradeRepository.findById(gradeId).get();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Teacher currentUser = teacherRepository.findByEmail(authentication.getName()).get();

        if (!assignment.getCourse().getTeachers().contains(currentUser)) {
            return ResponseEntity.status(FORBIDDEN).body(
                    new ErrorResponse("TEACHER_DOESNT_HAVE_PRIVILEGES_TO_THIS_COURSE",
                            "This teacher cannot modify course."));
        }

        assignment.getGrades().remove(grade);

        assignmentRepository.save(assignment);
        gradeRepository.delete(grade);

        return ResponseEntity.ok().build();
    }
}