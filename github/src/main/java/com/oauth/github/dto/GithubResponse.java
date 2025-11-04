package com.oauth.github.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GithubResponse {

    private String providerId;
    private String userName;
    private String email;
    private String pictureUrl;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
}
