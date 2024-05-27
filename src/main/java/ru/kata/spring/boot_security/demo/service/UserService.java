package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    User findByUsername(String username);

    User findById(Long id);

    User findUserById(Long userId);

    UserDetails loadUserByUsername(String email);

    List<User> allUsers();

    List<Role> findAll();

    List<Role> findAllById(Iterable<Long> ids);

    boolean userIsAdmin(User user);

    boolean userIsUser(User user);

    void saveUser(User user);

    public void updateUser(User userUpdate);

    void deleteUser(Long userId);
}
