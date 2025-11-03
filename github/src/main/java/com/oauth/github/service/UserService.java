package com.oauth.github.service;

import com.oauth.github.dto.GithubResponse;
import com.oauth.github.model.User;
import com.oauth.github.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService  extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        GithubResponse newUser = new GithubResponse();
        newUser.setUserName(oAuth2User.getAttribute("login"));
        Integer providerId = oAuth2User.getAttribute("id");
        newUser.setProviderId(String.valueOf(providerId));
        newUser.setEmail(oAuth2User.getAttribute("email"));
        newUser.setPictureUrl(oAuth2User.getAttribute("avatar_url"));

        return oAuth2User;
    }

    private void loginUser(GithubResponse githubResponse) {
        Optional<User> userOption = userRepository.findByProviderId(githubResponse.getProviderId());

        if (userOption.isPresent()) {
            //update existing user
            User existringUser = userOption.get();
            existringUser.setUserName(githubResponse.getUserName());
            existringUser.setEmail(githubResponse.getEmail());
            existringUser.setProviderId(githubResponse.getProviderId());
            existringUser.setProfilePicture(githubResponse.getPictureUrl());
            existringUser.setLastLoginAt(LocalDateTime.now());
            userRepository.save(existringUser);
        } else {
            //create new user
            User newUser = new User();
            newUser.setUserName(githubResponse.getUserName());
            newUser.setProviderId(githubResponse.getProviderId());
            newUser.setEmail(githubResponse.getEmail());
            newUser.setProfilePicture(githubResponse.getPictureUrl());
            newUser.setCreatedAt(LocalDateTime.now());
            userRepository.save(newUser);
        }
    }
}
