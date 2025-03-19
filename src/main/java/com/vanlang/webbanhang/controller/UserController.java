package com.vanlang.webbanhang.controller;

import com.vanlang.webbanhang.model.User;
import com.vanlang.webbanhang.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "users/login";
    }

    @GetMapping("/register")
    public String register(@NotNull Model model) {
        model.addAttribute("user", new User());
        return "users/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user,
                           @NotNull BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "users/register";
        }
        userService.save(user);
        userService.setDefaultRole(user.getUsername());
        return "redirect:/login";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = userService.findAllUsers();
        for (User user : users) {
            String roleNames = user.getRoles().stream()
                    .map(role -> role.getName())
                    .collect(Collectors.joining(", "));
            user.setRoleNames(roleNames);
        }
        model.addAttribute("users", users);
        long userCount = userService.countUsers(); // Đếm số lượng người dùng
        model.addAttribute("userCount", userCount); // Thêm số lượng người dùng vào model
        return "users/user-list";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "users/edit-user"; // Chuyển hướng đến template edit-user.html trong thư mục users
    }

    @PostMapping("/users/update")
    public String updateUser(@ModelAttribute User user) {
        userService.update(user);
        return "redirect:/users"; // Redirect đến danh sách người dùng sau khi cập nhật
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return "redirect:/users"; // Redirect đến danh sách người dùng sau khi xóa
    }
}
