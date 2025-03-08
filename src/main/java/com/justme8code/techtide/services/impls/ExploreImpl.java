package com.justme8code.techtide.services.impls;

import com.justme8code.techtide.dtos.BlogPostDto;
import com.justme8code.techtide.mappers.BlogPostMapper;
import com.justme8code.techtide.models.BlogPost;
import com.justme8code.techtide.repositories.BlogPostRepository;
import com.justme8code.techtide.services.interfaces.Explore;
import com.justme8code.techtide.specifications.BlogPostSpecification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExploreImpl implements Explore {

    private final BlogPostRepository blogPostRepository;
    private final BlogPostMapper blogPostMapper;

    public ExploreImpl(BlogPostRepository blogPostRepository, BlogPostMapper blogPostMapper) {
        this.blogPostRepository = blogPostRepository;
        this.blogPostMapper = blogPostMapper;
    }

    @Override
    public List<BlogPostDto> explore(String keyword) {
        List<BlogPost> blogPosts;
        if(keyword.equals("latest")){
            blogPosts=blogPostRepository.
                    findAll(BlogPostSpecification.latest(keyword, LocalDateTime.now().minusWeeks(2)));
            return blogPosts.stream().map(blogPostMapper::toDto).toList();
        }
        blogPosts = blogPostRepository.findAll(BlogPostSpecification.hasKeyword(keyword));
        return blogPosts.stream().map(blogPostMapper::toDto).toList();
    }

}