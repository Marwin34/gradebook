package pl.edu.agh.zarzecze.gradebook.controllers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.zarzecze.gradebook.model.ErrorResponse;
import pl.edu.agh.zarzecze.gradebook.model.User;
import pl.edu.agh.zarzecze.gradebook.model.auth.*;
import pl.edu.agh.zarzecze.gradebook.repository.SubjectRepository;
import pl.edu.agh.zarzecze.gradebook.repository.UserRepository;
import pl.edu.agh.zarzecze.gradebook.security.JwtTokenUtil;
import pl.edu.agh.zarzecze.gradebook.services.CustomUserDetailsService;
import pl.edu.agh.zarzecze.gradebook.services.PasswordService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@CrossOrigin
public class AllAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private SubjectRepository subjectRepository;

    
    @RequestMapping(value = "/api/auth/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody LoginRequest authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        User user = userRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Email " + authenticationRequest.getEmail() + " not found"));

        // create new refresh token if not exists
        if (user.getRefreshToken() == null || user.getRefreshToken().length() == 0) {
            user.setRefreshToken(UUID.randomUUID().toString());
            userRepository.save(user);
        }

        final JwtToken accessToken = jwtTokenUtil.generateAccessToken(userDetails);
        final String refreshToken = user.getRefreshToken();

        List<String> authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken, authorities));
    }

    
    @RequestMapping(value = "/api/auth/refresh-token", method = RequestMethod.POST)
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest authenticationRequest) {
        String username = authenticationRequest.getEmail();
        String refreshToken = authenticationRequest.getRefreshToken();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Email " + username + " not found"));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (refreshToken.equals(user.getRefreshToken())) {
            final JwtToken token = jwtTokenUtil.generateAccessToken(userDetails);
            return ResponseEntity.ok(new RefreshTokenResponse(token, refreshToken));
        }

        return new ResponseEntity<Object>("Access denied", new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
    
    @PostMapping(value = "/api/auth/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordModel changePasswordModel) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String currentUserEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(currentUserEmail).get();

        if (!passwordService.passwordMatches(changePasswordModel.getCurrentPassword(), currentUser.getPassword())) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("INVALID_PASSWORD",
                            "Current password is invalid."));
        }

        currentUser.setPassword(passwordService.encodePassword(changePasswordModel.getNewPassword()));
        userRepository.save(currentUser);

        return ResponseEntity.ok().build();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    private static class ChangePasswordModel {

        private String currentPassword;
        private String newPassword;

    }
}