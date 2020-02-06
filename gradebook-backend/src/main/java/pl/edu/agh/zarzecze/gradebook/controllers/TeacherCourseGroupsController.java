package pl.edu.agh.zarzecze.gradebook.controllers;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.zarzecze.gradebook.model.Course;
import pl.edu.agh.zarzecze.gradebook.model.ErrorResponse;
import pl.edu.agh.zarzecze.gradebook.model.Group;
import pl.edu.agh.zarzecze.gradebook.model.Teacher;
import pl.edu.agh.zarzecze.gradebook.repository.CourseRepository;
import pl.edu.agh.zarzecze.gradebook.repository.GroupRepository;
import pl.edu.agh.zarzecze.gradebook.repository.TeacherRepository;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.FORBIDDEN;


@RestController
public class TeacherCourseGroupsController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Data
    private static class AddGroupToCourseRequest {
        private long groupId;
    }
    
    @PreAuthorize("hasAnyRole('TEACHER')")
    @GetMapping("/api/teacher/courses/{courseId}/groups")
    public Iterable<TeacherGroupController.GroupURIResponse> getAllCourseGroups(@PathVariable(value = "courseId") Long id) {
        Course course = courseRepository.findById(id).get();
        return groupRepository.findAll().stream()
                .filter(e -> e.getCourses().contains(course))
                .map(TeacherGroupController.GroupURIResponse::new)
                .collect(toList());
    }
    
    @PreAuthorize("hasAnyRole('TEACHER')")
    @PostMapping("/api/teacher/courses/{courseId}/groups")
    public ResponseEntity<?> addGroupToCourse(@PathVariable(value = "courseId") Long id,
                                              @RequestBody AddGroupToCourseRequest addGroupToCourseRequest) {

        Group group = groupRepository.findById(addGroupToCourseRequest.groupId).get();
        Course course = courseRepository.findById(id).get();

        if (course.getGroups().contains(group)) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("GROUP_ALREADY_IN_COURSE",
                            "This group is already in this course."));
        }

        course.getGroups().add(group);

        courseRepository.save(course);
        groupRepository.save(group);

        return ResponseEntity.ok().build();
    }
    
    @PreAuthorize("hasAnyRole('TEACHER')")
    @GetMapping("/api/teacher/courses/{courseId}/groups/{groupId}")
    public Group getCourseGroup(@PathVariable(value = "courseId") Long id,
                                @PathVariable(value = "groupId") Long groupId) {
        Course course = courseRepository.findById(id).get();
        Group group = groupRepository.findById(groupId).get();

        return group;
    }

    @PreAuthorize("hasAnyRole('TEACHER')")
    @DeleteMapping("/api/teacher/courses/{courseId}/groups/{groupId}")
    public ResponseEntity<?> removeGroupFromCourse(@PathVariable(value = "courseId") Long id,
                                                   @PathVariable(value = "groupId") Long groupId) {

        Course course = courseRepository.findById(id).get();
        Group group = groupRepository.findById(groupId).get();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Teacher currentUser = teacherRepository.findByEmail(authentication.getName()).get();

        if (!course.getTeachers().contains(currentUser)) {
            return ResponseEntity.status(FORBIDDEN).body(
                    new ErrorResponse("TEACHER_DOESNT_HAVE_PRIVILEGES_TO_THIS_COURSE",
                            "This teacher cannot modify course."));
        }


        if (!course.getGroups().contains(group)) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("GROUP_IS_NOT_A_PART_OF_COURSE",
                            "This group doesn't take part in course."));
        }

        course.getGroups().remove(group);
        group.getCourses().remove(course);

        courseRepository.save(course);
        groupRepository.save(group);

        return ResponseEntity.ok().build();
    }
}
