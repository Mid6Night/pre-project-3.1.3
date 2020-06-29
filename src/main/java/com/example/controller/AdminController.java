package com.example.controller;

import com.example.entity.Role;
import com.example.entity.User;
import com.example.repos.RoleRepo;
import com.example.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.*;

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
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        model.addAttribute("currentUser", user);
        model.addAttribute("users", users);
        model.addAttribute("allRoles",roleRepo.findAll());
        for (Role role : user.getRoles()) {
            if (role.getName().equals("ADMIN")) {
                model.addAttribute("admin", false);
                return "user";
            }
        }
        model.addAttribute("admin", true);
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
                          @RequestParam String[] role,
                          @RequestParam String email) {
        User user = new User(firstName, lastName, email);
        user.setPassword(password);
        user.setRoles(new HashSet<>());
        for (int i = 0; i< role.length; i++){
            user.getRoles().add(roleRepo.getById(Long.valueOf(role[i])));
        }
        userService.save(user);
        return "redirect:/admin";
    }

    @RequestMapping(value = "/{id}/delete")
    public String deleteUser(@PathVariable(name = "id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }

    @RequestMapping(path = "/update", method = RequestMethod.GET)
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

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@RequestParam(name = "id") Long id,
                         @RequestParam(name = "firstName") String firstName,
                         @RequestParam(name = "lastName") String lastName,
                         @RequestParam(name = "password") String password,
                         @RequestParam(name = "email") String email,
                         @RequestParam String[] role,
                         @RequestParam(name = "admin", required = false) boolean admin) {
        Optional<User> userOptional = userService.findById(id);
        User user = userOptional.get();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setRoles(new HashSet<>());
        for (int i = 0; i< role.length; i++){
            user.getRoles().add(roleRepo.getById(Long.valueOf(role[i])));
        }
        user.setPassword(password);
        userService.save(user);
        return "redirect:/admin";
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Optional<User> findOne (@PathVariable(name = "id") Long id) {
        return userService.findById(id);
    }
}