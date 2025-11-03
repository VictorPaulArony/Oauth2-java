package com.auth.google.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String providerId;

    @Column(nullable = false, unique = true)
    private String email;

    private String name;

    private String profilePicture;

    private LocalDateTime createdAt;

    private LocalDateTime lastLoginAt;

}
