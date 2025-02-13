package com.justme8code.techtide.util;

import io.jsonwebtoken.Claims;

public interface Authorizer {
    Claims validateToken(String token);
    String generateToken(String userId);
}
