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
import java.util.Set;

import static org.springframework.http.HttpStatus.FORBIDDEN;


@RestController
public class TeacherAssignmentGroupsController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Data
    private static class AddGroupToAssignmentRequest {
        private Long groupId;
    }

    @PreAuthorize("hasAnyRole('TEACHER')")
    @GetMapping("/api/teacher/courses/{courseId}/assignments/{assignmentId}/groups")
    public Set<Group> getAssignmentGroups(@PathVariable(value = "courseId") Long id,
                                          @PathVariable(value = "assignmentId") Long assignmentId) {
        return assignmentRepository.findById(assignmentId).get().getGroups();
    }
    
    @PreAuthorize("hasAnyRole('TEACHER')")
    @PostMapping("/api/teacher/courses/{courseId}/assignments/{assignmentId}/groups")
    public ResponseEntity<?> addGroupToAssignment(@PathVariable(value = "courseId") Long courseId,
                                                  @PathVariable(value = "assignmentId") Long assignmentId,
                                                  @RequestBody AddGroupToAssignmentRequest addGroupToAssignmentRequest) {

        Course course = courseRepository.findById(courseId).get();
        Assignment assignment = assignmentRepository.findById(assignmentId).get();
        Group group = groupRepository.findById(addGroupToAssignmentRequest.getGroupId()).get();

        if (!course.getGroups().contains(group)) {
            return ResponseEntity.status(FORBIDDEN).body(
                    new ErrorResponse("GROUP_NOT_IN_THIS_COURSE",
                            "This group isn't in this course."));
        }

        if (assignment.getGroups().contains(group)) {
            return ResponseEntity.status(FORBIDDEN).body(
                    new ErrorResponse("GROUP_ALREADY_IN_THIS_ASSIGNMENT",
                            "This group is already in this assignment."));
        }

        assignment.getGroups().add(group);

        assignmentRepository.save(assignment);
        return ResponseEntity.ok().build();
    }
    
    @PreAuthorize("hasAnyRole('TEACHER')")
    @DeleteMapping("/api/teacher/courses/{courseId}/assignments/{assignmentId}/groups/{groupId}")
    public ResponseEntity<?> removeGroupFromAssignment(@PathVariable(value = "courseId") Long id,
                                                       @PathVariable(value = "assignmentId") Long assignmentId,
                                                       @PathVariable(value = "groupId") Long groupId) {

        Assignment assignment = assignmentRepository.findById(assignmentId).get();
        Group group = groupRepository.findById(groupId).get();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Teacher currentUser = teacherRepository.findByEmail(authentication.getName()).get();

        assignment.getGroups().remove(group);

        group.getStudents().stream()
                .flatMap(e -> e.getGrades().stream())
                .filter(e -> e.getAssignment().equals(assignment))
                .forEach(e -> gradeRepository.delete(e));

        assignmentRepository.save(assignment);

        return ResponseEntity.ok().build();
    }
}
