package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@RestController
public class RestAdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public RestAdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(value = "/admin/getUsers")
    public List<User> getUsers() {
        return userService.allUsers();
    }

    @GetMapping(value = "/admin/getRoles")
    public List<Role> getRoles() {
        return roleService.findAll();
    }

    @PostMapping(value = "/admin/addUser")
    public void addUser(@RequestBody User user) {
        userService.saveUser(user);
    }

    @GetMapping(value = "/admin/getUser/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    @DeleteMapping(value = "/admin/deleteUser/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PutMapping(value = "/admin/updateUser/{id}")
    @ResponseBody
    public void updateUser(@PathVariable Long id, @RequestBody User user) {
        userService.updateUser(id, user);
    }

}
