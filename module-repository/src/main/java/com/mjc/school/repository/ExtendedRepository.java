package com.mjc.school.repository;

import com.mjc.school.repository.model.Comment;
import com.mjc.school.repository.model.News;
import com.mjc.school.repository.model.Tag;

import java.util.List;

public interface ExtendedRepository {
    List<News> readNewsByParams(List<Long> tagsIds, String tagName, String authorName, String title, String content);

    List<Tag> getTagsByNewsId(Long newsId);

    List<Comment> getCommentsByNewsId(Long newsId);
}