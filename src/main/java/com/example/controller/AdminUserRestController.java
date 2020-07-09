package com.example.controller;

import com.example.entity.User;
import com.example.repository.RoleRepo;
import com.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/user")
public class AdminUserRestController {

    private UserService userService;
    private RoleRepo roleRepo;

    public AdminUserRestController(UserService userService, RoleRepo roleRepo) {
        this.userService = userService;
        this.roleRepo = roleRepo;
    }

    @PostMapping(value = "/getAll")
    public ResponseEntity<List<User>> getAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<String> addUser(@ModelAttribute("userModel") User user,
                                          @RequestParam Long[] role) {
        userService.save(user, role);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping(value = "/findOne")
    public ResponseEntity<User> findOne(@RequestParam Long id) {
        return new ResponseEntity<>(userService.findUserById(id), HttpStatus.OK);
    }

    @PostMapping(value = "/delete")
    public ResponseEntity<String> deleteUser(@ModelAttribute("userModel") User user) {
        userService.deleteById(user.getId());
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping(value = "/update")
    public ResponseEntity<String> update(@ModelAttribute("userModel") User user,
                                         @RequestParam Long[] role) {
        userService.save(user, role);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
