package com.justme8code.techtide.services.interfaces;

import com.justme8code.techtide.dtos.BlogPostDto;

import java.util.List;

public interface Explore {
    // explore content based on input words.

    List<BlogPostDto> explore(String keyword);
}
