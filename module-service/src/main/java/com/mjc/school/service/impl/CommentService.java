package com.mjc.school.service.impl;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.model.Comment;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.ServiceCommentRequestDto;
import com.mjc.school.service.dto.ServiceCommentResponseDto;
import com.mjc.school.service.mapper.CommentMapper;
import com.mjc.school.service.validator.annotations.ValidateInput;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService implements BaseService<ServiceCommentRequestDto, ServiceCommentResponseDto, Long> {

    private final BaseRepository<Comment, Long> commentRepository;
    private final CommentMapper commentMapper;

    public CommentService(@Qualifier("commentRepository")
                          BaseRepository<Comment, Long> commentRepository,
                          CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    public List<ServiceCommentResponseDto> readAll() {
        List<ServiceCommentResponseDto> serviceCommentResponseDtoList = new ArrayList<>();
        for (Comment comment : commentRepository.readAll()) {
            serviceCommentResponseDtoList.add(commentMapper.mapModelToResponseDto(comment));
        }
        return serviceCommentResponseDtoList;
    }

    @Override
    public List<ServiceCommentResponseDto> readAll(Integer pageNumber, Integer pageSize, String sortBy) {
        List<ServiceCommentResponseDto> serviceCommentResponseDtoList = new ArrayList<>();
        for (Comment comment : commentRepository.readAll(pageNumber, pageSize, sortBy)) {
            serviceCommentResponseDtoList.add(commentMapper.mapModelToResponseDto(comment));
        }
        return serviceCommentResponseDtoList;
    }

    @Override
    @ValidateInput
    public ServiceCommentResponseDto readById(Long id) {
        Comment comment = commentRepository.readById(id).get();
        return commentMapper.mapModelToResponseDto(comment);
    }

    @Override
    @ValidateInput
    public ServiceCommentResponseDto create(ServiceCommentRequestDto serviceCommentRequestDto) {
        ServiceCommentResponseDto newComment =
                new ServiceCommentResponseDto(
                        null,
                        serviceCommentRequestDto.getContent(),
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        serviceCommentRequestDto.getNewsId());
        Comment comment = commentMapper.mapResponseDtoToModel(newComment);
        return commentMapper.mapModelToResponseDto(commentRepository.create(
                comment
        ));
    }

    @Override
    @ValidateInput
    public ServiceCommentResponseDto update(ServiceCommentRequestDto commentUpdateRequest) {
        return commentMapper.mapModelToResponseDto(
                commentRepository.update(
                        commentMapper.mapRequestDtoToModel(commentUpdateRequest)
                ));
    }

    @Override
    @ValidateInput
    public boolean deleteById(Long id) {
        return commentRepository.deleteById(id);
    }
}
