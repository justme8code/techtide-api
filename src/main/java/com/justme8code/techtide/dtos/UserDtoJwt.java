package com.justme8code.techtide.dtos;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link com.justme8code.techtide.models.User}
 */
public record UserDtoJwt(String id, String username, Set<String> roleRoleNames) implements Serializable {
}