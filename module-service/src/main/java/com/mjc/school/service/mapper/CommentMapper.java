package com.mjc.school.service.mapper;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.model.Comment;
import com.mjc.school.repository.model.News;
import com.mjc.school.service.dto.ServiceCommentRequestDto;
import com.mjc.school.service.dto.ServiceCommentResponseDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    private final ModelMapper mapper = new ModelMapper();
    private final BaseRepository<News, Long> newsRepository;

    public CommentMapper(BaseRepository<News, Long> newsRepository) {
        this.newsRepository = newsRepository;
    }

    public ServiceCommentResponseDto mapModelToResponseDto(Comment comment) {
        return mapper.map(comment, ServiceCommentResponseDto.class);
    }

    public Comment mapResponseDtoToModel(ServiceCommentResponseDto commentDto) {
        Comment commentModel = mapper.map(commentDto, Comment.class);
        commentModel.setNews(newsRepository.readById(commentDto.getNewsId()).get());
        return commentModel;
    }

    public Comment mapRequestDtoToModel(ServiceCommentRequestDto commentRequestDto) {
        Comment commentModel = mapper.map(commentRequestDto, Comment.class);
        commentModel.setNews(newsRepository.readById(commentRequestDto.getNewsId()).get());
        return commentModel;
    }
}
