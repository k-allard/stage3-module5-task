package com.mjc.school.controller.mapper;

import com.mjc.school.controller.dto.AuthorRequestDto;
import com.mjc.school.controller.dto.AuthorResponseDto;
import com.mjc.school.controller.dto.CommentRequestDto;
import com.mjc.school.controller.dto.CommentResponseDto;
import com.mjc.school.controller.dto.NewsRequestDto;
import com.mjc.school.controller.dto.NewsResponseDto;
import com.mjc.school.controller.dto.TagDto;
import com.mjc.school.service.dto.ServiceAuthorRequestDto;
import com.mjc.school.service.dto.ServiceAuthorResponseDto;
import com.mjc.school.service.dto.ServiceCommentRequestDto;
import com.mjc.school.service.dto.ServiceCommentResponseDto;
import com.mjc.school.service.dto.ServiceNewsRequestDto;
import com.mjc.school.service.dto.ServiceNewsResponseDto;
import com.mjc.school.service.dto.ServiceTagDto;
import org.modelmapper.ModelMapper;

public class ServiceToWebDTOMapper {

    private final ModelMapper mapper = new ModelMapper();

    public NewsResponseDto mapServiceNewsResponseDto(
            ServiceNewsResponseDto serviceNewsResponseDto) {
        return mapper.map(serviceNewsResponseDto, NewsResponseDto.class);
    }

    public ServiceNewsRequestDto mapNewsRequestDto(NewsRequestDto newsRequestDto) {
        return mapper.map(newsRequestDto, ServiceNewsRequestDto.class);
    }

    public AuthorResponseDto mapServiceAuthorResponseDto(
            ServiceAuthorResponseDto serviceAuthorResponseDto) {
        return mapper.map(serviceAuthorResponseDto, AuthorResponseDto.class);
    }

    public ServiceAuthorRequestDto mapAuthorRequestDto(
            AuthorRequestDto authorRequestDto) {
        return mapper.map(authorRequestDto, ServiceAuthorRequestDto.class);
    }

    public TagDto mapServiceTagDto(ServiceTagDto serviceTagDto) {
        return mapper.map(serviceTagDto, TagDto.class);
    }

    public ServiceTagDto mapTagToServiceDto(TagDto tagDto) {
        return mapper.map(tagDto, ServiceTagDto.class);
    }

    public CommentResponseDto mapServiceCommentResponseDto(ServiceCommentResponseDto responseDto) {
        return mapper.map(responseDto, CommentResponseDto.class);
    }

    public ServiceCommentRequestDto mapCommentRequestDto(CommentRequestDto dtoRequest) {
        return mapper.map(dtoRequest, ServiceCommentRequestDto.class);
    }
}
