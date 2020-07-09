package com.example.controller;

import com.example.entity.User;
import com.example.repository.RoleRepo;
import com.example.repository.UserRepo;
import com.example.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private UserService userService;
    private RoleRepo roleRepo;

    public AdminController(UserService userService, RoleRepo roleRepo){
        this.userService = userService;
        this.roleRepo = roleRepo;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String userList(ModelMap model) {
        List<User> users = userService.findAll();

        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        model.addAttribute("currentUser", user);
        model.addAttribute("users", users);
        model.addAttribute("allRoles", roleRepo.findAll());
        model.addAttribute("userModel", new User());

        return "user";
    }

}