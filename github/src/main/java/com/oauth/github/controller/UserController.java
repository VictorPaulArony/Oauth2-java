package com.oauth.github.controller;

import com.oauth.github.model.User;
import com.oauth.github.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal != null) {
            model.addAttribute("username", principal.getAttribute("login"));
            model.addAttribute("email",principal.getAttribute("email") );
            model.addAttribute("avatarUrl", principal.getAttribute("avatar_url"));

            // Get user info from database
            Optional<User> user = userRepository.findByUserName(principal.getAttribute("login"));
            user.ifPresent( u -> {
            model.addAttribute("providerId", u.getId());
            model.addAttribute("createdAt",u.getCreatedAt());
            model.addAttribute("lastLoginAt", u.getLastLoginAt());
            });
        }
        return "index";
    }

}
