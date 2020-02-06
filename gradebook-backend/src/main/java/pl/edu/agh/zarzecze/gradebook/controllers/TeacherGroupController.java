package pl.edu.agh.zarzecze.gradebook.controllers;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.agh.zarzecze.gradebook.model.ErrorResponse;
import pl.edu.agh.zarzecze.gradebook.model.Group;
import pl.edu.agh.zarzecze.gradebook.model.Student;
import pl.edu.agh.zarzecze.gradebook.repository.GroupRepository;
import pl.edu.agh.zarzecze.gradebook.repository.StudentRepository;
import pl.edu.agh.zarzecze.gradebook.services.PasswordService;

import java.net.URI;

import static java.util.stream.Collectors.toList;


@RestController
public class TeacherGroupController {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordService passwordService;
    
    @PreAuthorize("hasAnyRole('TEACHER')")
    @GetMapping("/api/teacher/groups")
    public Iterable<GroupURIResponse> getAllGroups() {
        return groupRepository.findAll().stream()
                .map(GroupURIResponse::new)
                .collect(toList());
    }

    @PreAuthorize("hasAnyRole('TEACHER')")
    @PostMapping("/api/teacher/groups")
    public ResponseEntity<?> addGroup(@RequestBody NewGroupRequest newGroupRequest) {
        Group group = new Group(newGroupRequest.getName());

        groupRepository.save(group);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(group.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }
    
    @PreAuthorize("hasAnyRole('TEACHER')")
    @DeleteMapping("/api/teacher/groups/{groupId}")
    public ResponseEntity<?> deleteGroup(@PathVariable(value = "groupId") Long id) {
        Group group = groupRepository.findById(id).get();
        groupRepository.delete(group);

        return ResponseEntity.ok().build();
    }

    @Data
    @NoArgsConstructor
    private static class NewGroupRequest {
        private String name;
    }

    @Data
    @NoArgsConstructor
    static class GroupURIResponse {
        private String name;
        private Long id;

        public GroupURIResponse(Group group) {
            this.name = group.getName();
            this.id = group.getId();
        }
    }

    @PreAuthorize("hasAnyRole('TEACHER')")
    @GetMapping("/api/teacher/groups/{groupId}")
    public Group getGroup(@PathVariable(value = "groupId") Long id) {
        return groupRepository.findById(id).get();
    }

    @PreAuthorize("hasAnyRole('TEACHER')")
    @PostMapping("/api/teacher/groups/{groupId}/students")
    public ResponseEntity<?> addToGroup(@PathVariable(value = "groupId") Long id,
                                        @RequestBody AddStudentToGroupRequest studentRequest) {
        Group group = groupRepository.findById(id).get();
        Student student = studentRepository.findById(studentRequest.getStudentId()).get();
        if (group.getStudents().contains(student)) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("STUDENT_ALREADY_IN_GROUP",
                            "This student is already in this group."));
        }
        group.getStudents().add(student);
        student.getGroups().add(group);

        groupRepository.save(group);
        studentRepository.save(student);

        return ResponseEntity.ok().build();
    }

    @Data
    private static class AddStudentToGroupRequest {
        private long studentId;
    }

    @PreAuthorize("hasAnyRole('TEACHER')")
    @DeleteMapping("/api/teacher/groups/{groupId}/students/{studentId}")
    public ResponseEntity<?> removeFromGroup(@PathVariable(value = "groupId") Long id,
                                             @PathVariable(value = "studentId") Long studentId) {

        Group group = groupRepository.findById(id).get();
        Student student = studentRepository.findById(studentId).get();
        if (!group.getStudents().contains(student)) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("STUDENT_NOT_IN_GROUP",
                            "This student doesn't belong to this group."));
        }

        group.getStudents().remove(student);
        student.getGroups().remove(group);

        groupRepository.save(group);
        studentRepository.save(student);

        return ResponseEntity.ok().build();
    }
}