package com.example.controller;

import com.example.entity.User;
import com.example.repos.RoleRepo;
import com.example.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class RestUserController {
    @Autowired
    private UserRepo userService;

    @Autowired
    private RoleRepo roleRepo;

    @PostMapping(value = "/getAll")
    public List<User> getAll() {
        return (List<User>) userService.findAll();
    }

    @PostMapping(value = "/add")
    public void addUser(@ModelAttribute("userModel") User user,
                        @RequestParam String[] role) {
        user.setRoles(new HashSet<>());
        for (int i = 0; i < role.length; i++) {
            user.getRoles().add(roleRepo.getById(Long.valueOf(role[i])));
        }
        userService.save(user);
    }

    @PostMapping(value = "/findOne")
    public User findOne(@RequestParam Long id) {
        return userService.findUserById(id);
    }

    @PostMapping(value = "/delete")
    public void deleteUser(@ModelAttribute("userModel") User user) {
        userService.deleteById(user.getId());
    }

    @PostMapping(value = "/update")
    public void update(@ModelAttribute("userModel") User user,
                       @RequestParam String[] role) {
        user.setRoles(new HashSet<>());
        for (int i = 0; i < role.length; i++) {
            user.getRoles().add(roleRepo.getById(Long.valueOf(role[i])));
        }
        userService.save(user);
    }
}
