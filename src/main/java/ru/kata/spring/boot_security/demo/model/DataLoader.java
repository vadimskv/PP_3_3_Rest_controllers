package ru.kata.spring.boot_security.demo.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.HashSet;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataLoader(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        Role adminRole = new Role("ROLE_ADMIN");
        Role userRole = new Role("ROLE_USER");
        roleRepository.save(adminRole);
        roleRepository.save(userRole);

        User admin = new User("admin", "admin", 35, passwordEncoder.encode("admin"), "admin@mail.ru");
        admin.setRoles(new HashSet<>(List.of(adminRole, userRole)));
        userRepository.save(admin);

        User user = new User("user", "user", 30, passwordEncoder.encode("user"), "user@mail.ru");
        user.setRoles(new HashSet<>(List.of(userRole)));
        userRepository.save(user);
    }
}