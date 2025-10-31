package com.auth.google.service;

import com.auth.google.dto.UserRequest;
import com.auth.google.model.User;
import com.auth.google.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService  extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequester) {
        OAuth2User oAuth2User = super.loadUser(userRequester);

        //get the user info from the Google
        String provider = userRequester.getClientRegistration().getRegistrationId();
        String providerId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String pictureUrl = oAuth2User.getAttribute("picture");

        UserRequest userRequest = new UserRequest();
        userRequest.setEmail(email);
        userRequest.setProvider(provider);
        userRequest.setProviderId(providerId);
        userRequest.setName(name);
        userRequest.setPicture(pictureUrl);

        // Process OAuth2 user (register or update)
        processOauth2User(userRequest);
        return oAuth2User;

    }

    public  void processOauth2User(UserRequest userRequest) {
        Optional<User> userOptional = userRepository.findByProviderId(userRequest.getProviderId());

        if (userOptional.isPresent()) {
            //update existing user
            User existingUser = userOptional.get();
            existingUser.setName(userRequest.getName());
            existingUser.setProfilePicture(userRequest.getPicture());
            existingUser.setLastLoginAt(LocalDateTime.now());
            userRepository.save(existingUser);
        }else {
            // Register new user
            User newUser = new User();
            newUser.setEmail(userRequest.getEmail());
            newUser.setName(userRequest.getName());
            newUser.setProfilePicture(userRequest.getPicture());
            newUser.setProviderId(userRequest.getProviderId());
            userRepository.save(newUser);
        }
    }
}
