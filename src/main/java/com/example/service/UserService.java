package com.example.service;

import com.example.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    void save(User user, Long[] s);

    void save (User user);

    List<User> findAll();

    User findUserById(Long id);

    void deleteById(Long id);


}
