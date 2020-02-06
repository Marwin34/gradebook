package pl.edu.agh.zarzecze.gradebook.model.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>Used for converting json request to java data.</p>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RefreshTokenRequest {
    private String email;
    private String refreshToken;
}