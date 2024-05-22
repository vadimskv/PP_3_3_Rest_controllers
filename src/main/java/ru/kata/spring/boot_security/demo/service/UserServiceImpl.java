package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public List<Role> findAllById(Iterable<Long> ids) {
        return roleRepository.findAllById(ids);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        User user = optionalUser.get();
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }

    public User findUserById(Long userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        return userFromDb.orElse(new User());
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void saveUser(User user) {
        Optional<User> optionalUserFromDB = userRepository.findByUsername(user.getUsername());
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

    @Transactional
    public void updateUser(User updatedUser, List<Long> selectedRoleIds) {
        User user = userRepository.findById(updatedUser.getId()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            if (!updatedUser.getPassword().equals(user.getPassword())) {
                user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }
        }
        Set<Role> selectedRoles = new HashSet<>();
        for (Long roleId : selectedRoleIds) {
            roleRepository.findById(roleId).ifPresent(selectedRoles::add);
        }
        user.setRoles(selectedRoles);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);

    }
}
