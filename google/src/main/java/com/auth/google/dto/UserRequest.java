package com.auth.google.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String providerId;
    private String provider;
    private String name;
    private String email;
    private String picture;
}
