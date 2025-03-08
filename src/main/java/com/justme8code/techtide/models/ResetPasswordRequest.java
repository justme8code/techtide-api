package com.justme8code.techtide.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResetPasswordRequest {

    private String username;
    private String password;
    private String newPassword;
}
