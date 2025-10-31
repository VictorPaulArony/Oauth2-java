package com.auth.google.controllar;

import com.auth.google.model.User;
import com.auth.google.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.Optional;

@RequestMapping("/")
@Controller()
@RequiredArgsConstructor
public class GoogleOauth {

    private final UserRepository userRepository;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/profile")
    public String dashboard(@AuthenticationPrincipal OAuth2User principal, Model model, OAuth2AuthenticationToken authentication) {
        if (principal != null) {
            String email = principal.getAttribute("email");
            String name = principal.getAttribute("name");
            String picture = principal.getAttribute("picture");

            model.addAttribute("name", name);
            model.addAttribute("email", email);
            model.addAttribute("picture", picture);
            model.addAttribute("provider", authentication.getAuthorizedClientRegistrationId());

            // Get user info from database
            Optional<User> user = userRepository.findByEmail(email);
            user.ifPresent(u -> {
                model.addAttribute("userId", u.getId());
                model.addAttribute("createdAt", u.getCreatedAt());
                model.addAttribute("lastLoginAt", u.getLastLoginAt());
            });
        }
        return "home";
    }

}
