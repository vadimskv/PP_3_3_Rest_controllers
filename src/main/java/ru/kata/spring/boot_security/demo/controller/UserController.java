package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            request.getSession().invalidate();
        }
        return "redirect:/login";
    }

    @ModelAttribute
    public void getInfo(Principal principal, Model model) {
        model.addAttribute("allUsers", userService.allUsers());
        String username = principal.getName();
        User user = userService.findByUsername(username);
        model.addAttribute("isAdmin", userService.userIsAdmin(user));
        model.addAttribute("isUser", userService.userIsUser(user));
        model.addAttribute("userInfo", user);

    }

    @GetMapping("/admin")
    public String getUsers() {
        return "admin";
    }

    @GetMapping("/user")
    public String showUserProfile() {
        return "user";
    }

    @GetMapping()
    public String newUser() {
        return "parts/admin/users_table/new";
    }


    @ModelAttribute
    public void newUser(Model model) {
        model.addAttribute("new_user", new User());
        List<Role> roles = userService.findAll();
        model.addAttribute("roles", roles);
    }

    @PostMapping("/admin/save")
    public String addUser(@ModelAttribute("user") User user, @RequestParam("roles") Long[] roles) {
        List<Role> selectedRoles = userService.findAllById(Arrays.asList(roles));
        user.setRoles(selectedRoles);
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/")
    public String edit() {
        return "parts/admin/users_table/users_table";
    }

    @RequestMapping(value = "/admin/edit/{id}")
    public String updateUser(@ModelAttribute("user") User updateUser) {
        userService.updateUser(updateUser);
        return "redirect:/admin/";
    }


    @PostMapping("admin/delete")
    public String remove(@RequestParam(value = "id", required = false) Long id, Model model) {
        if (id == null) {
            return "redirect:/admin";
        }
        model.addAttribute("user", userService.findUserById(id));
        userService.deleteUser(id);
        return "redirect:/admin";
    }


}
