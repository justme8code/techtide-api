package com.justme8code.techtide.services;

import com.justme8code.techtide.dtos.BlogPostDto;
import com.justme8code.techtide.dtos.BlogPostDtoWithContent;
import com.justme8code.techtide.exceptions.UnExpectedException;
import com.justme8code.techtide.mappers.BlogPostMapper;
import com.justme8code.techtide.models.BlogPost;
import com.justme8code.techtide.models.User;
import com.justme8code.techtide.repositories.BlogPostRepository;
import com.justme8code.techtide.repositories.UserRepository;
import com.justme8code.techtide.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BlogPostService {

    private final BlogPostRepository blogPostRepository;
    private final BlogPostMapper blogPostMapper;
    private final UserRepository userRepository;

    @Autowired
    public BlogPostService(BlogPostRepository blogPostRepository, BlogPostMapper blogPostMapper, UserRepository userRepository) {
        this.blogPostRepository = blogPostRepository;
        this.blogPostMapper = blogPostMapper;
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasRole({'ROLE_ADMIN'})")
    public BlogPostDto createBlogPost(BlogPost blogPost) {
        String userId = SecurityUtils.getCurrentUserId();
        User user =  userRepository.findByUsernameEqualsIgnoreCaseOrIdEqualsIgnoreCase(userId,userId)
                .orElseThrow(() -> new AccessDeniedException("Access denied, while creating blog post"));
        blogPost.setUser(user);
        return  blogPostMapper.toDto(blogPostRepository.save(blogPost));
    }

    public BlogPostDto update(BlogPost blogPost) {
        BlogPost blogPostOptional = blogPostRepository.findById(blogPost.getId())
                .orElseThrow(() -> new UsernameNotFoundException("Blog post not found"));
       blogPostOptional.setContent(blogPost.getContent());
       blogPostOptional.setTitle(blogPost.getTitle());
       blogPostOptional.setCoverImageUrl(blogPost.getCoverImageUrl());
       return blogPostMapper.toDto(blogPostRepository.save(blogPostOptional));
    }

    public BlogPostDtoWithContent findById(Long id) {
        BlogPost blogPost = blogPostRepository.findById(id)
                .orElseThrow(() -> new UnExpectedException("Blog post not found"));
        return blogPostMapper.toDto1(blogPost);
    }


    public Page<BlogPostDto> fetchPaginatedBlogs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BlogPost> blogPostPage = blogPostRepository.findAll(pageable);
        Set<BlogPost> uniqueBlogPosts = new HashSet<>(blogPostPage.getContent());
        List<BlogPostDto> blogPostDtos = uniqueBlogPosts.stream()
                .map(blogPostMapper::toDto)
                .toList();
        return new PageImpl<>(blogPostDtos, pageable, blogPostPage.getTotalElements());
    }

}
