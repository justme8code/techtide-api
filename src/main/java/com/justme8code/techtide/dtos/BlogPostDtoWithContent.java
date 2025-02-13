package com.justme8code.techtide.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.justme8code.techtide.models.BlogPost}
 */
public record BlogPostDtoWithContent(Long id, String title, String coverImageUrl, String description,String content,
                                     LocalDateTime publishedOn, LocalDateTime modifiedOn) implements Serializable {
}