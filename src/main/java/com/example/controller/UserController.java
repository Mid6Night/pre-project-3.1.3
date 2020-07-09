package com.example.controller;

import com.example.entity.Role;
import com.example.entity.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


@Controller
@RequestMapping("/user")

public class UserController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String userInfo(ModelMap model) {
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        model.addAttribute("currentUser",user);
        return "user-info";
    }
}
