package com.justme8code.techtide.specifications;

import com.justme8code.techtide.models.BlogPost;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class BlogPostSpecification {

    private BlogPostSpecification(){}
    public static Specification<BlogPost> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.like(root.get("title"), "%" + keyword + "%"),
                criteriaBuilder.like(root.get("description"), "%" + keyword + "%")
        );
    }

    public static Specification<BlogPost> latest(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.greaterThanOrEqualTo(root.get("publishedOn"), date)
        );
    }
}