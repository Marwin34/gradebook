package pl.edu.agh.zarzecze.gradebook.controllers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import pl.edu.agh.zarzecze.gradebook.model.User;
import pl.edu.agh.zarzecze.gradebook.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;


@RestController
public class AllCurrentUserController {

    @AllArgsConstructor
    @Getter
    class AuthenticatedUser {
        String email;
        String firstName;
        String lastName;
        List<String> authorities;
    }

    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/api/current-user")
    public ResponseEntity<?> currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String currentUserEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(currentUserEmail).get();

        List<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new AuthenticatedUser(
                currentUser.getEmail(),
                currentUser.getFirstName(),
                currentUser.getLastName(),
                authorities));

    }

}
