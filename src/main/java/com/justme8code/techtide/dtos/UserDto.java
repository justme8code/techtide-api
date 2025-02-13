package com.justme8code.techtide.dtos;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.justme8code.techtide.models.User}
 */
public record UserDto(String id, String username, List<BlogPostDto> blogPosts) implements Serializable {
}