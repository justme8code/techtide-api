package com.justme8code.techtide.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLogin {
    private final String username;
    private final String password;

    public UserLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
