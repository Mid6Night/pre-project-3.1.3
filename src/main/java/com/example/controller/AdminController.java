package com.example.controller;

import com.example.entity.Role;
import com.example.entity.User;
import com.example.repos.RoleRepo;
import com.example.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @Autowired
    private UserRepo userService;

    @Autowired
    private RoleRepo roleRepo;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String userList(ModelMap model) {
        List<User> users = (List<User>) userService.findAll();
        model.addAttribute("users", users);
        return "user";
    }

    @RequestMapping(path = "/add", method = RequestMethod.GET)
    public String getCreatePage() {
        return "user-create";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addUser(@RequestParam String firstName,
                          @RequestParam String lastName,
                          @RequestParam String password,
                          @RequestParam String email) {
        User user = new User(firstName, lastName, email);
        user.setPassword(password);
        user.setRoles(new HashSet<>());
        user.getRoles().add(roleRepo.getByName("USER"));
        userService.save(user);
        return "redirect:/admin";
    }

    @RequestMapping(value = "/{id}/delete")
    public String deleteUser(@PathVariable(name = "id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }

    @RequestMapping(path = "/{id}/update", method = RequestMethod.GET)
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

    @RequestMapping(value = "/{id}/update", method = RequestMethod.POST)
    public String update(@PathVariable(name = "id") Long id,
                         @RequestParam(name = "firstName") String firstName,
                         @RequestParam(name = "lastName") String lastName,
                         @RequestParam(name = "password") String password,
                         @RequestParam(name = "email") String email,
                         @RequestParam(name = "admin", required = false) boolean admin) {
        Optional<User> userOptional = userService.findById(id);
        User user = userOptional.get();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        if (admin && !user.getRoles().contains(roleRepo.getByName("ADMIN"))) {
            user.getRoles().add(roleRepo.getByName("ADMIN"));
        } else if (!admin && user.getRoles().contains(roleRepo.getByName("ADMIN"))) {
            user.getRoles().remove(roleRepo.getByName("ADMIN"));
        }
        userService.save(user);
        return "redirect:/admin";
    }
}