package com.mjc.school.controller;

import com.mjc.school.controller.dto.AuthorResponseDto;
import com.mjc.school.controller.dto.CommentResponseDto;
import com.mjc.school.controller.dto.NewsResponseDto;
import com.mjc.school.controller.dto.TagDto;

import java.util.List;

public interface ExtendedController {
    AuthorResponseDto readAuthorByNewsId(Long id);

    List<TagDto> readTagsByNewsId(Long id);

    List<CommentResponseDto> readCommentsByNewsId(Long id);

    List<NewsResponseDto> readNewsByParams(List<Long> tagsIds, String tagName, String authorName, String title, String content);
}
