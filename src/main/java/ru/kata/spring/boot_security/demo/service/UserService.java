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
    UserDetails loadUserByUsername(String username);
    List<User> allUsers();
    List<Role> findAll();
    List<Role> findAllById(Iterable<Long> ids);
    void saveUser(User user);
    void updateUser(User updatedUser, List<Long> selectedRoleIds);
    void deleteUser(Long userId);
}
