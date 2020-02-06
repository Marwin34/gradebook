package pl.edu.agh.zarzecze.gradebook.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import pl.edu.agh.zarzecze.gradebook.model.User;
import pl.edu.agh.zarzecze.gradebook.repository.UserRepository;

import javax.annotation.PostConstruct;

@Component
public class DatabaseInit {

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    private void postConstruct() {
        //create default user if not exists
        if (userRepository.findByEmail("admin@admin.com").isEmpty()) {
            User admin = new User("admin@admin.com",
                    new BCryptPasswordEncoder().encode("admin"),
                    "admin",
                    "admin");
            admin.getRoles().add(User.Role.ROLE_ADMIN);
            userRepository.save(admin);
        }
    }
}
