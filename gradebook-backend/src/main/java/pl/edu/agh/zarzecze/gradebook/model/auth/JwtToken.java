package pl.edu.agh.zarzecze.gradebook.model.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;


@AllArgsConstructor
@Getter
public class JwtToken {
    String token;
    Date expirationDate;
}
