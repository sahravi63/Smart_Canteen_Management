package com.example.canteen.security;

import com.example.canteen.model.User;
import com.example.canteen.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;

    public CustomSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userService.findByUsername(username).orElse(null);

        if (user == null) {
            response.sendRedirect("/login?error=user_not_found");
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("user", user);

        response.sendRedirect("/dashboard");
    }
}
