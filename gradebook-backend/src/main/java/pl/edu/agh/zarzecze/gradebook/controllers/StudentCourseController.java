package pl.edu.agh.zarzecze.gradebook.controllers;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.zarzecze.gradebook.model.*;
import pl.edu.agh.zarzecze.gradebook.repository.CourseRepository;
import pl.edu.agh.zarzecze.gradebook.repository.StudentRepository;
import pl.edu.agh.zarzecze.gradebook.repository.SubjectRepository;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;


@RestController
public class StudentCourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectRepository subjectRepository;
    
    @PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping("/api/student/courses")
    public Iterable<CourseResponse> getAllCourses() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        Student currentUser = studentRepository.findByEmail(currentUserEmail).get();
        return courseRepository.findAll().stream()
                .filter(e -> CollectionUtils.containsAny(e.getGroups(), currentUser.getGroups()))
                .map(CourseResponse::new)
                .collect(toList());
    }

    @Getter
    @Setter
    private static class CourseResponse {

        private Long id;
        private String name;
        private String subject;
        private List<Assignment> assignments;
        private Set<Classes> classes;
        private Set<Teacher> teachers;
        private Set<Group> groups;

        CourseResponse(Course sourceCourse) {
            id = sourceCourse.getId();
            name = sourceCourse.getName();
            subject = sourceCourse.getSubject().getName();
            assignments = sourceCourse.getAssignments();
            classes = sourceCourse.getClasses();
            teachers = sourceCourse.getTeachers();
            groups = sourceCourse.getGroups();
        }
    }

}
