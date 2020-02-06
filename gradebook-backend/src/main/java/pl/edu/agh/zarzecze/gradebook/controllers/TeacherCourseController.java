package pl.edu.agh.zarzecze.gradebook.controllers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.agh.zarzecze.gradebook.model.*;
import pl.edu.agh.zarzecze.gradebook.repository.CourseRepository;
import pl.edu.agh.zarzecze.gradebook.repository.SubjectRepository;
import pl.edu.agh.zarzecze.gradebook.repository.TeacherRepository;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toList;


@RestController
public class TeacherCourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SubjectRepository subjectRepository;
    
    @PreAuthorize("hasAnyRole('TEACHER')")
    @GetMapping("/api/teacher/courses")
    public Iterable<CourseResponse> getAllCourses() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        Teacher currentUser = teacherRepository.findByEmail(currentUserEmail).get();

        return courseRepository.findByTeachersContaining(currentUser).stream()
                .map(CourseResponse::new)
                .collect(toList());
    }

    
    @PreAuthorize("hasAnyRole('TEACHER')")
    @PostMapping("/api/teacher/courses")
    public ResponseEntity<?> createCourse(@RequestBody NewCourseRequest newCourseRequest) {

        Optional<Subject> subject = subjectRepository.findById(newCourseRequest.subjectId);

        if (subject.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("SUBJECT_DOESNT_EXIST", "Subject doesn't exist."));
        }

        Course newCourse = new Course(newCourseRequest.getName());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        Teacher currentUser = teacherRepository.findByEmail(currentUserEmail).get();

        newCourse.getTeachers().add(currentUser);
        newCourse.setSubject(subject.get());

        subject.get().addCourse(newCourse);

        courseRepository.save(newCourse);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newCourse.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class NewCourseRequest {

        private String name;
        private Long subjectId;

    }

    @Getter
    @Setter
    public static class CourseResponse {

        private Long id;
        private String name;
        private String subject;
        private List<Assignment> assignments;
        private Set<Classes> classes;
        private Set<Teacher> teachers;
        private Set<Group> groups;

        public CourseResponse(Course sourceCourse) {
            id = sourceCourse.getId();
            name = sourceCourse.getName();
            subject = sourceCourse.getSubject().getName();
            assignments = sourceCourse.getAssignments();
            classes = sourceCourse.getClasses();
            teachers = sourceCourse.getTeachers();
            groups = sourceCourse.getGroups();
        }
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/api/teacher/courses/{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable(value = "courseId") Long id) {
        Course course = courseRepository.findById(id).get();

        courseRepository.delete(course);
        return ResponseEntity.ok().build();
    }

}
