package pl.edu.agh.zarzecze.gradebook.controllers;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.agh.zarzecze.gradebook.mail.MailSender;
import pl.edu.agh.zarzecze.gradebook.model.ErrorResponse;
import pl.edu.agh.zarzecze.gradebook.model.Student;
import pl.edu.agh.zarzecze.gradebook.model.User;
import pl.edu.agh.zarzecze.gradebook.repository.StudentRepository;
import pl.edu.agh.zarzecze.gradebook.repository.UserRepository;
import pl.edu.agh.zarzecze.gradebook.services.PasswordService;

import java.net.URI;
import java.util.Optional;


@RestController
public class TeacherStudentController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordService passwordService;
    
    @PreAuthorize("hasAnyRole('TEACHER')")
    @GetMapping("/api/teacher/students")
    public Iterable<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    @PreAuthorize("hasAnyRole('TEACHER')")
    @PostMapping("/api/teacher/students")
    public ResponseEntity<?> addStudent(@RequestBody NewStudentRequest newStudentRequest) {
        Optional<User> user = userRepository.findByEmail(newStudentRequest.getEmail());
        if (user.isPresent()) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("STUDENT_ALREADY_EXISTS",
                            "Student with this e-mail already exists."));
        }
        String password = passwordService.generateNewPassword();

        Student student = new Student(
                newStudentRequest.getEmail(),
                passwordService.encodePassword(password),
                newStudentRequest.getFirstName(),
                newStudentRequest.getLastName());

        studentRepository.save(student);

        MailSender.sendPassword(newStudentRequest.getEmail(), newStudentRequest.getFirstName(),
                newStudentRequest.getLastName(), password);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(student.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('TEACHER')")
    @DeleteMapping("/api/teacher/students/{studentId}")
    public ResponseEntity deleteStudent(@PathVariable Long studentId) {
        studentRepository.delete(studentRepository.findById(studentId).get());
        return ResponseEntity.noContent().build();
    }

    @Data
    private static class NewStudentRequest {

        private String email;
        private String firstName;
        private String lastName;
    }
}