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
import pl.edu.agh.zarzecze.gradebook.repository.AssignmentRepository;
import pl.edu.agh.zarzecze.gradebook.repository.CourseRepository;
import pl.edu.agh.zarzecze.gradebook.repository.TeacherRepository;

import java.net.URI;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.FORBIDDEN;


@RestController
public class TeacherAssignmentController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Data
    private static class AddAssignmentToCourseRequest {
        private String name;
    }

    @PreAuthorize("hasAnyRole('TEACHER')")
    @GetMapping("/api/teacher/courses/{courseId}/assignments")
    public List<Assignment> getAllCourseGroups(@PathVariable(value = "courseId") Long id) {
        Course course = courseRepository.findById(id).get();
        return assignmentRepository.findAll().stream()
                .filter(e -> e.getCourse().equals(course))
                .collect(toList());
    }

    @PreAuthorize("hasAnyRole('TEACHER')")
    @PostMapping("/api/teacher/courses/{courseId}/assignments")
    public ResponseEntity<?> addAssignmentToCourse(@PathVariable(value = "courseId") Long id,
                                                   @RequestBody AddAssignmentToCourseRequest addAssignmentToCourseRequest) {

        Course course = courseRepository.findById(id).get();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Teacher currentUser = teacherRepository.findByEmail(authentication.getName()).get();

        if (!course.getTeachers().contains(currentUser)) {
            return ResponseEntity.status(FORBIDDEN).body(
                    new ErrorResponse("TEACHER_DOESNT_HAVE_PRIVILEGES_TO_THIS_COURSE",
                            "This teacher cannot modify course."));
        }

        Assignment newAssignment = new Assignment(addAssignmentToCourseRequest.name);
        newAssignment.setCourse(course);

        course.getAssignments().add(newAssignment);

        assignmentRepository.save(newAssignment);
        courseRepository.save(course);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newAssignment.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('TEACHER')")
    @DeleteMapping("/api/teacher/courses/{courseId}/assignments/{assignmentId}")
    public ResponseEntity<?> removeGroupFromCourse(@PathVariable(value = "courseId") Long id,
                                                   @PathVariable(value = "assignmentId") Long assignmentId) {

        Course course = courseRepository.findById(id).get();
        Assignment assignment = assignmentRepository.findById(assignmentId).get();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Teacher currentUser = teacherRepository.findByEmail(authentication.getName()).get();

        if (!course.getTeachers().contains(currentUser)) {
            return ResponseEntity.status(FORBIDDEN).body(
                    new ErrorResponse("TEACHER_DOESNT_HAVE_PRIVILEGES_TO_THIS_COURSE",
                            "This teacher cannot modify course."));
        }


        if (!course.getAssignments().contains(assignment)) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("ASSIGNMENT_IS_NOT_A_PART_OF_COURSE",
                            "This assignment doesn't take part in course."));
        }

        course.getAssignments().remove(assignment);

        courseRepository.save(course);
        assignmentRepository.delete(assignment);

        return ResponseEntity.ok().build();
    }
    
    @PreAuthorize("hasAnyRole('TEACHER')")
    @GetMapping("/api/teacher/courses/{courseId}/assignments/{assignmentId}")
    public ResponseEntity<?> getAssignment(@PathVariable(value = "courseId") Long id,
                                           @PathVariable(value = "assignmentId") Long assignmentId) {

        Course course = courseRepository.findById(id).get();
        Assignment assignment = assignmentRepository.findById(assignmentId).get();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Teacher currentUser = teacherRepository.findByEmail(authentication.getName()).get();

        if (!course.getTeachers().contains(currentUser)) {
            return ResponseEntity.status(FORBIDDEN).body(
                    new ErrorResponse("TEACHER_DOESNT_HAVE_PRIVILEGES_TO_THIS_COURSE",
                            "This teacher cannot modify course."));
        }

        if (!course.getAssignments().contains(assignment)) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("ASSIGNMENT_IS_NOT_A_PART_OF_COURSE",
                            "This assignment doesn't take part in course."));
        }

        return ResponseEntity.ok(assignment);
    }
}
