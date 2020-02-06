package pl.edu.agh.zarzecze.gradebook.model.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.edu.agh.zarzecze.gradebook.model.auth.JwtToken;


@AllArgsConstructor
@Getter
public class RefreshTokenResponse {
    private final JwtToken token;
    private final String refreshToken;
}