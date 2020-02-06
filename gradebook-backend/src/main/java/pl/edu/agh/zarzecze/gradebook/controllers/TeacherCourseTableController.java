package pl.edu.agh.zarzecze.gradebook.controllers;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.zarzecze.gradebook.model.*;
import pl.edu.agh.zarzecze.gradebook.repository.CourseRepository;
import pl.edu.agh.zarzecze.gradebook.repository.SubjectRepository;
import pl.edu.agh.zarzecze.gradebook.repository.TeacherRepository;

import java.util.*;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableList;


@RestController
public class TeacherCourseTableController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    
    @PreAuthorize("hasAnyRole('TEACHER')")
    @GetMapping("/api/teacher/courses/{courseId}/table")
    public ResponseEntity<?> createCourse(@PathVariable(value = "courseId") Long id) {
        Course course = courseRepository.findById(id).get();
        return ResponseEntity.ok(new CourseTableResponse(course));
    }

    @Data
    public static class CourseTableResponse {
        private List<Object> headers = new ArrayList<>();
        private List<StudentRow> rows = new ArrayList<>();

        
        public CourseTableResponse(Course sourceCourse) {
            List<Student> courseStudents = sourceCourse.getGroups().stream()
                    .flatMap(e -> e.getStudents().stream())
                    .distinct()
                    .sorted(comparing(User::getLastName))
                    .collect(toList());

            sourceCourse.getAssignments().forEach(
                    e -> headers.add(new HeaderColumn(e))
            );

            this.rows = courseStudents.stream()
                    .map(e -> new StudentRow(e, sourceCourse.getAssignments()))
                    .collect(toUnmodifiableList());

        }

        @Data
        private static class StudentRow {
            private final String studentName;
            private final Long studentId;
            private final ArrayList<StudentResult> grades;

            public StudentRow(Student student, List<Assignment> assignments) {
                studentName = student.getFirstName() + " " + student.getLastName();
                studentId = student.getId();
                grades = new ArrayList<>();
                for (Assignment assignment : assignments) {
                    if (assignment.getGrades().stream()
                            .anyMatch(e -> e.getStudent().equals(student))) {

                        Grade grade = assignment.getGrades().stream()
                                .filter(e -> e.getStudent().equals(student))
                                .findFirst()
                                .get();
                        grades.add(StudentResult.of(grade));
                    } else if (assignment.getGroups().stream()
                            .anyMatch(e -> e.getStudents().contains(student))) {
                        grades.add(StudentResult.NONE);
                    } else {
                        grades.add(StudentResult.NOT_IN_ASSIGNMENT);
                    }
                }

            }

        }

        @Data
        private static class StudentResult {
            public static final StudentResult NOT_IN_ASSIGNMENT = new StudentResult("NOT_IN_ASSIGNMENT", -1L, "");
            public static final StudentResult NONE = new StudentResult("NONE", -1L, "");
            private String gradeType;
            private Long gradeId;
            private String gradeText;


            public StudentResult(String gradeType, Long gradeId, String gradeText) {
                this.gradeType = gradeType;
                this.gradeId = gradeId;
                this.gradeText = gradeText;
            }

            public static StudentResult of(Grade grade) {
                return new StudentResult(
                        grade.getGradeValue().toString(),
                        grade.getId(),
                        grade.getGradeValue().getValue());
            }
        }

        @Data
        private static class HeaderColumn {
            private final String assignmentName;
            private final Long assignmentId;

            public HeaderColumn(Assignment e) {
                this.assignmentName = e.getName();
                this.assignmentId = e.getId();
            }
        }
    }
}
