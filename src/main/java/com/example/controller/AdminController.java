package com.example.controller;

import com.example.entity.Role;
import com.example.entity.User;
import com.example.repos.RoleRepo;
import com.example.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepo userService;

    @Autowired
    private RoleRepo roleRepo;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String userList(ModelMap model) {
        List<User> users = (List<User>) userService.findAll();
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        model.addAttribute("currentUser", user);
        model.addAttribute("users", users);
        model.addAttribute("allRoles", roleRepo.findAll());
        model.addAttribute("userModel", new User());
        for (Role role : user.getRoles()) {
            if (role.getName().equals("ADMIN")) {
                model.addAttribute("admin", false);
                return "user";
            }
        }
        model.addAttribute("admin", true);
        return "user";
    }

}