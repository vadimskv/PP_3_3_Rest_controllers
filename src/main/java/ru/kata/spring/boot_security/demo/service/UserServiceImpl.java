package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + email));
    }


    public User findUserById(Long id) {
        Optional<User> userFromDb = userRepository.findById(id);
        return userFromDb.orElse(new User());
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public boolean userIsAdmin(User user) {
        return user.getRolesString().contains("ADMIN");
    }

    public boolean userIsUser(User user) {
        return user.getRolesString().contains("USER");
    }

    @Transactional
    public void saveUser(User user) {
        Optional<User> optionalUserFromDB = userRepository.findByEmail(user.getUsername());
        if (optionalUserFromDB.isPresent()) {
            throw new IllegalArgumentException("User with this username already exists");
        }

        Collection<Long> roleIds = user.getRoles().stream()
                .map(Role::getId)
                .collect(Collectors.toList());

        List<Role> roles = roleRepository.findAllById(roleIds);
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void updateUser(Long id, User user) {

        User userAfterUpdate = findUserById(user.getId());
        if (userAfterUpdate != null) {
            userAfterUpdate.setEmail(user.getEmail());

            if (user.getPassword().isEmpty()) {
                userAfterUpdate.setPassword(user.getPassword());
            } else if (!userAfterUpdate.getPassword().equals(user.getPassword())) {
                userAfterUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            userAfterUpdate.setAge(user.getAge());
            userAfterUpdate.setFirstname(user.getFirstname());
            userAfterUpdate.setLastname(user.getLastname());
            userAfterUpdate.setRoles(user.getRoles());

            userRepository.save(userAfterUpdate);
        } else {
            throw new UsernameNotFoundException("User isn't found");
        }

    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);

    }
}
