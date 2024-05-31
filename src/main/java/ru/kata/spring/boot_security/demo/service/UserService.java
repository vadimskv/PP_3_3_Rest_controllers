package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    User findByEmail(String email);

    User findUserById(Long id);

    List<User> allUsers();

    boolean userIsAdmin(User user);

    boolean userIsUser(User user);

    void saveUser(User user);

    void updateUser(Long id, User user);

    void deleteUser(Long userId);
}
