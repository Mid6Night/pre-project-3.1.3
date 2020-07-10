package com.example.service;

import com.example.entity.Role;
import com.example.entity.User;
import com.example.repository.RoleRepo;
import com.example.repository.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
public class UserServiceIml implements UserDetailsService, UserService {

    private UserRepo userRepo;

    private RoleRepo roleRepo;


    public UserServiceIml(UserRepo userRepo, RoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findUserByEmail(username);
    }

    public void save(User user, Long[] role) {
        user.setRoles(new HashSet<>(Arrays.asList(getRoles(role))));
        userRepo.save(user);
    }

    public Role[] getRoles(Long[] id) {
        Role[] roles = new Role[id.length];
        for (int i = 0; i < id.length; i++) {
            roles[i] = roleRepo.getById(id[i]);
        }
        return roles;
    }

    @Override
    public void save(User user) {
        userRepo.save(user);
    }

    @Override
    public List<User> findAll() {
        return (List<User>) userRepo.findAll();
    }

    @Override
    public User findUserById(Long id) {
        return userRepo.findUserById(id);
    }

    @Override
    public void deleteById(Long id) {
        userRepo.deleteById(id);
    }
}