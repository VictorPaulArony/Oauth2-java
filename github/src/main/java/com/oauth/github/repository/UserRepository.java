package com.oauth.github.repository;

import com.oauth.github.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByProviderId(String providerId);
    Optional<User> findByUserName(String userName);

}
