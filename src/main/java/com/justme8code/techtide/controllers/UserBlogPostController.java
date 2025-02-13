package com.justme8code.techtide.controllers;

import com.justme8code.techtide.dtos.BlogPostDto;
import com.justme8code.techtide.models.BlogPost;
import com.justme8code.techtide.services.BlogPostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/blogs")
public class UserBlogPostController {
    private final BlogPostService blogPostService;

    public UserBlogPostController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    @PostMapping
    public ResponseEntity<BlogPostDto> requestCreateBlogPost(@RequestBody BlogPost blogPost) {
        return new ResponseEntity<>(blogPostService.createBlogPost(blogPost), HttpStatus.CREATED);
    }
}
