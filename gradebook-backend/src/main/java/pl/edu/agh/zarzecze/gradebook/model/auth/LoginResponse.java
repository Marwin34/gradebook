package pl.edu.agh.zarzecze.gradebook.model.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.edu.agh.zarzecze.gradebook.model.auth.JwtToken;

import java.util.List;


@AllArgsConstructor
@Getter
public class LoginResponse {
    private final JwtToken accessToken;
    private final String refreshToken;
    private final List<String> authorities;
}