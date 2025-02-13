package com.justme8code.techtide.controllers;

import com.justme8code.techtide.dtos.BlogPostDto;
import com.justme8code.techtide.dtos.BlogPostDtoWithContent;
import com.justme8code.techtide.services.BlogPostService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blogs")
public class BlogPostController {

    private final BlogPostService blogPostService;

    public BlogPostController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    @GetMapping
    public ResponseEntity<Page<BlogPostDto>> requestBlogPosts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<BlogPostDto> blogPosts = blogPostService.fetchPaginatedBlogs(page, size);
        return ResponseEntity.ok(blogPosts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogPostDtoWithContent> requestBlogPost(@PathVariable long id) {
        return ResponseEntity.ok(blogPostService.findById(id));
    }
}
