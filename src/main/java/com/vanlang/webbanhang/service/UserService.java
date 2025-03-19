package com.vanlang.webbanhang.service;

import com.vanlang.webbanhang.Role;
import com.vanlang.webbanhang.model.User;
import com.vanlang.webbanhang.repository.IRoleRepository;
import com.vanlang.webbanhang.repository.IUserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    public void save(@NotNull User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }

    public void setDefaultRole(String username) {
        userRepository.findByUsername(username).ifPresentOrElse(user -> {
            user.getRoles().add(roleRepository.findRoleById(Role.USER.value));
            userRepository.save(user);
        }, () -> {
            throw new UsernameNotFoundException("User not found");
        });
    }

    public void update(@NotNull User user) {
        User existingUser = userRepository.findById(String.valueOf(user.getId())).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        existingUser.setEmail(user.getEmail());
        existingUser.setPhone(user.getPhone());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        }
        userRepository.save(existingUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .accountExpired(!user.isAccountNonExpired())
                .accountLocked(!user.isAccountNonLocked())
                .credentialsExpired(!user.isCredentialsNonExpired())
                .disabled(!user.isEnabled())
                .build();
    }

    public Optional<User> findByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(String.valueOf(id)).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void deleteUserById(Long id) {
        if (userRepository.existsById(String.valueOf(id))) {
            userRepository.deleteById(String.valueOf(id));
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    public long countUsers() {
        return userRepository.count();
    }

}
