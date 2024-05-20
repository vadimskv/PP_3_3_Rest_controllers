package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Controller
public class UserController {
    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public UserController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/admin")
    public String findAll(Model model) {
        model.addAttribute("admin", userService.allUsers());
        return "admin";
    }

    @GetMapping("/user")
    public String showUserProfile(Principal principal, Model model) {
        String username = principal.getName();
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/admin/user")
    public String getUserProfile(@RequestParam("id") Long userId, Model model) {
        User user = userService.findById(userId);
        if (user == null) {
            return "redirect:/admin";
        }
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/admin/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("roles", roles);
        return "new";
    }

    @PostMapping("/admin/save")
    public String addUser(@ModelAttribute("user") User user, @RequestParam("roles") Long[] roles) {
        List<Role> selectedRoles = roleRepository.findAllById(Arrays.asList(roles));
        user.setRoles(selectedRoles);
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/edit")
    public String editUser(@RequestParam(value = "id", required = false) Long id, Model model) {
        if (id == null) {
            return "redirect:/admin";
        }
        model.addAttribute("user", userService.findUserById(id));
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("roles", roles);
        return "edit";
    }

    @PostMapping("/admin/update")
    public String updateUser(@ModelAttribute("user") User user,
                             @RequestParam("selectedRoleIds") List<Long> selectedRoleIds) {
        userService.updateUser(user, selectedRoleIds);
        return "redirect:/admin";
    }

    @GetMapping("admin/remove")
    public String remove(@RequestParam(value = "id", required = false) Long id, Model model) {
        if (id == null) {
            return "redirect:/admin";
        }
        model.addAttribute("user", userService.findUserById(id));
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
