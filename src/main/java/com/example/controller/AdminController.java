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

    @GetMapping(path = "/add")
    public String getCreatePage() {
        return "user-create";
    }

    @PostMapping(value = "/add")
    @ResponseStatus(HttpStatus.CREATED)
    public String addUser(@ModelAttribute("userModel") User user,
                          @RequestParam String[] role) {
        user.setRoles(new HashSet<>());
        for (int i = 0; i < role.length; i++) {
            user.getRoles().add(roleRepo.getById(Long.valueOf(role[i])));
        }
        userService.save(user);
        return "redirect:/admin";
    }

    @PostMapping(value = "/delete")
    public String deleteUser(@ModelAttribute("userModel") User user) {
        userService.deleteById(user.getId());
        return "redirect:/admin";
    }

    @GetMapping(path = "/update")
    public String getUpdatePage(@PathVariable(name = "id") Long id, Model model) {
        Optional<User> userOptional = userService.findById(id);
        User user = userOptional.get();
        model.addAttribute("firstName", user.getFirstName());
        model.addAttribute("lastName", user.getLastName());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("password", user.getPassword());
        model.addAttribute("id", user.getId());
        for (Role role : user.getRoles()) {
            if (role.equals(roleRepo.getByName("ADMIN"))) {
                model.addAttribute("admin", true);
            }
        }
        return "user-update";
    }


    @PostMapping(value = "/update")
    public String update(@ModelAttribute("userModel") User user,
                         @RequestParam String[] role) {
        user.setRoles(new HashSet<>());
        for (int i = 0; i < role.length; i++) {
            user.getRoles().add(roleRepo.getById(Long.valueOf(role[i])));
        }
        userService.save(user);
        return "redirect:/admin";
    }

    @PostMapping(value = "findOne/{id}")
    @ResponseBody
    public Optional<User> findOne(@PathVariable(name = "id") Long id) {
        return userService.findById(id);
    }
}