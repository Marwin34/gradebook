package pl.edu.agh.zarzecze.gradebook.controllers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.zarzecze.gradebook.mail.MailSender;
import pl.edu.agh.zarzecze.gradebook.model.ErrorResponse;
import pl.edu.agh.zarzecze.gradebook.model.Subject;
import pl.edu.agh.zarzecze.gradebook.model.Teacher;
import pl.edu.agh.zarzecze.gradebook.model.User;
import pl.edu.agh.zarzecze.gradebook.repository.TeacherRepository;
import pl.edu.agh.zarzecze.gradebook.repository.UserRepository;
import pl.edu.agh.zarzecze.gradebook.services.PasswordService;

import java.util.Optional;


@RestController
public class AdminTeacherController {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordService passwordService;

    
    @GetMapping("/api/teachers")
    @PreAuthorize("isAuthenticated()")
    public Iterable<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @PostMapping("/api/teachers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity addNewTeacher(@RequestBody NewTeacherRequest newTeacherRequest) {
        Optional<User> user = userRepository.findByEmail(newTeacherRequest.getEmail());
        if (user.isPresent()) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("TEACHER_ALREADY_EXISTS",
                            "Teacher with this e-mail already exists."));
        } else {
            String password = passwordService.generateNewPassword();

            Teacher teacher = new Teacher(
                    newTeacherRequest.getEmail(),
                    passwordService.encodePassword(password),
                    newTeacherRequest.getFirstName(),
                    newTeacherRequest.getLastName());

            teacherRepository.save(teacher);

            MailSender.sendPassword(newTeacherRequest.getEmail(), newTeacherRequest.getFirstName(),
                    newTeacherRequest.getLastName(), password);

            return ResponseEntity.ok(new NewTeacherResponse(password));
        }
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/api/teachers/{teacherId}")
    public ResponseEntity<?> deleteSubject(@PathVariable(value = "teacherId") Long id) {
        Teacher teacher = teacherRepository.findById(id).get();

        teacherRepository.delete(teacher);

        return ResponseEntity.ok().build();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class NewTeacherRequest {

        private String email;
        private String firstName;
        private String lastName;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class NewTeacherResponse {

        private String password;

    }
}
