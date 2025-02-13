package com.justme8code.techtide.mappers;

import com.justme8code.techtide.dtos.BlogPostDto;
import com.justme8code.techtide.dtos.BlogPostDtoWithContent;
import com.justme8code.techtide.models.BlogPost;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BlogPostMapper {
    BlogPost toEntity(BlogPostDto blogPostDto);

    BlogPostDto toDto(BlogPost blogPost);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    BlogPost partialUpdate(BlogPostDto blogPostDto, @MappingTarget BlogPost blogPost);

    BlogPost toEntity(BlogPostDtoWithContent blogPostDtoWithContent);

    BlogPostDtoWithContent toDto1(BlogPost blogPost);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    BlogPost partialUpdate(BlogPostDtoWithContent blogPostDtoWithContent, @MappingTarget BlogPost blogPost);
}