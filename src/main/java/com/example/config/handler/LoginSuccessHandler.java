package com.example.config.handler;

import com.example.entity.Role;
import com.example.entity.User;
import com.example.repos.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();

        for (Role role : user.getRoles()) {
            if (role.getName().equals("ADMIN")) {
                httpServletResponse.sendRedirect("/admin");
                return;
            }
        }
        httpServletResponse.sendRedirect("/user");

    }
}