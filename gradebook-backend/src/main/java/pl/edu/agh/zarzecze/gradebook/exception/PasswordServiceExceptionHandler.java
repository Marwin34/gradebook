package pl.edu.agh.zarzecze.gradebook.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.edu.agh.zarzecze.gradebook.model.ErrorResponse;
import pl.edu.agh.zarzecze.gradebook.services.PasswordService.PasswordNotMeetRequirementsException;

@ControllerAdvice
public class PasswordServiceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({PasswordNotMeetRequirementsException.class})
    public ResponseEntity<?> handlePasswordNotMeetRequirementsException(PasswordNotMeetRequirementsException ex) {

        return ResponseEntity.badRequest().body(
                new ErrorResponse("PASSWORD_DOESNT_MEET_REQUIREMENTS",
                        ex.getMessage()));
    }

}
