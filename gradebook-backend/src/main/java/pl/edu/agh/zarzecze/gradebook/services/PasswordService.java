package pl.edu.agh.zarzecze.gradebook.services;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class PasswordService {
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public String encodePassword(String password) {
        Pattern lowerLetter = Pattern.compile("[A-Z]");
        Pattern upperLetter = Pattern.compile("[a-z]");
        Pattern specialCharacter = Pattern.compile("[^A-Za-z0-9]");

        Matcher hasLowerLetter = lowerLetter.matcher(password);
        Matcher hasUpperLetter = upperLetter.matcher(password);
        Matcher hasSpecialCharacter = specialCharacter.matcher(password);

        if (password.length() < 8 || !hasLowerLetter.find()
                || !hasUpperLetter.find() || !hasSpecialCharacter.find()) {
            throw new PasswordNotMeetRequirementsException("Password must be at least 8 characters, " +
                    "with at least one lowercase and uppercase letter and one special character.");
        }

        return bCryptPasswordEncoder.encode(password);
    }

    public boolean passwordMatches(String rawPassword, String encodedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }

    public String generateNewPassword() {
        String password = RandomStringUtils.randomAlphanumeric(4);
        password += RandomStringUtils.random(2, "!@#$%^&*()-_=+[{]};:,<.>/?");
        password += RandomStringUtils.random(2, 'a', 'z');
        password += RandomStringUtils.random(2, 'A', 'Z');

        return shuffleString(password);
    }

    private String shuffleString(String word) {
        List<Character> characters = new ArrayList<>();
        for (char c : word.toCharArray())
            characters.add(c);

        Collections.shuffle(characters);

        StringBuilder stringBuilder = new StringBuilder();
        for (char character : characters)
            stringBuilder.append(character);
        return stringBuilder.toString();
    }

    public static class PasswordNotMeetRequirementsException extends RuntimeException {

        public PasswordNotMeetRequirementsException(String message) {
            super(message);
        }

    }
}
