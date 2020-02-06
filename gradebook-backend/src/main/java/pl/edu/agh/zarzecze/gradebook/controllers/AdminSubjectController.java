package pl.edu.agh.zarzecze.gradebook.controllers;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.zarzecze.gradebook.model.ErrorResponse;
import pl.edu.agh.zarzecze.gradebook.model.Subject;
import pl.edu.agh.zarzecze.gradebook.repository.SubjectRepository;

import java.util.Optional;


@RestController
public class AdminSubjectController {

    @Autowired
    private SubjectRepository subjectRepository;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/subjects")
    public Iterable<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/subjects")
    public ResponseEntity addNewSubject(@RequestBody NewSubjectRequest newSubjectRequest) {
        Optional<Subject> subject = subjectRepository.findByName(newSubjectRequest.name);

        if (subject.isPresent()) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("SUBJECT_ALREADY_EXISTS", "Subject with this name already exists."));
        } else {
            Subject newSubject = new Subject(newSubjectRequest.name);

            subjectRepository.save(newSubject);

            return ResponseEntity.ok().build();
        }
    }

    @Data
    private static class NewSubjectRequest {
        private String name;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/api/subjects/{subjectId}")
    public ResponseEntity<?> deleteSubject(@PathVariable(value = "subjectId") Long id) {
        Subject subject = subjectRepository.findById(id).get();

        subjectRepository.delete(subject);

        return ResponseEntity.ok().build();
    }
}
