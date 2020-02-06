package pl.edu.agh.zarzecze.gradebook.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
public class ErrorResponse {
    private Error error;

    public ErrorResponse(String code, String message) {
        error = new Error(code, message);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private class Error {
        private String code;
        private String message;
    }
}
