package com.mjc.school.controller.impl;

import com.mjc.school.controller.BaseController;
import com.mjc.school.controller.ExtendedController;
import com.mjc.school.controller.dto.AuthorResponseDto;
import com.mjc.school.controller.dto.CommentResponseDto;
import com.mjc.school.controller.dto.NewsRequestDto;
import com.mjc.school.controller.dto.NewsResponseDto;
import com.mjc.school.controller.dto.TagDto;
import com.mjc.school.controller.mapper.ServiceToWebDTOMapper;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.ExtendedService;
import com.mjc.school.service.dto.ServiceAuthorResponseDto;
import com.mjc.school.service.dto.ServiceCommentResponseDto;
import com.mjc.school.service.dto.ServiceNewsRequestDto;
import com.mjc.school.service.dto.ServiceNewsResponseDto;
import com.mjc.school.service.dto.ServiceTagDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = ("/news"))
public class NewsController implements BaseController<NewsRequestDto, NewsResponseDto, Long>, ExtendedController {
    private final BaseService<ServiceNewsRequestDto, ServiceNewsResponseDto, Long> newsService;
    private final ExtendedService extendedService;
    private final ServiceToWebDTOMapper mapper = new ServiceToWebDTOMapper();

    public NewsController(
            @Qualifier("newsService")
            BaseService<ServiceNewsRequestDto, ServiceNewsResponseDto, Long> newsService,
            ExtendedService extendedService) {
        this.newsService = newsService;
        this.extendedService = extendedService;
    }


    @Override
    @GetMapping
    public List<NewsResponseDto> readAll(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false, defaultValue = "3") Integer pageSize,
            @RequestParam(required = false) String sortBy) {
        List<NewsResponseDto> newsResponseDtoList = new ArrayList<>();
        for (ServiceNewsResponseDto serviceDto : newsService.readAll(pageNumber, pageSize, sortBy)) {
            newsResponseDtoList.add(mapper.mapServiceNewsResponseDto(serviceDto));
        }
        return newsResponseDtoList;
    }

    @GetMapping("/{id}")
    public NewsResponseDto readById(@PathVariable Long id) {
        return mapper.mapServiceNewsResponseDto(
                newsService.readById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewsResponseDto create(@RequestBody NewsRequestDto dtoRequest) {
        return mapper.mapServiceNewsResponseDto(
                newsService.create(mapper.mapNewsRequestDto(dtoRequest)));
    }

    @PatchMapping("/{id}")
    public NewsResponseDto update(@PathVariable Long id,
                                  @RequestBody NewsRequestDto dtoRequest) {
        return mapper.mapServiceNewsResponseDto(
                newsService.update(mapper.mapNewsRequestDto(dtoRequest)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        newsService.deleteById(id);
    }

    @GetMapping("/{id}/author")
    public AuthorResponseDto readAuthorByNewsId(@PathVariable Long id) {
        ServiceAuthorResponseDto responseDto = extendedService.readAuthorByNewsId(id);
        AuthorResponseDto response = null;
        if (responseDto.getId() != null)
            response = mapper.mapServiceAuthorResponseDto(responseDto);
        return response;
    }

    @GetMapping("/{id}/tags")
    public List<TagDto> readTagsByNewsId(@PathVariable Long id) {
        List<ServiceTagDto> serviceTagDtos = extendedService.readTagsByNewsId(id);
        List<TagDto> tagsList = new ArrayList<>();
        for (ServiceTagDto tag : serviceTagDtos) {
            tagsList.add(mapper.mapServiceTagDto(tag));
        }
        return tagsList;
    }

    @Override
    @GetMapping("/{id}/comments")
    public List<CommentResponseDto> readCommentsByNewsId(@PathVariable Long id) {
        List<ServiceCommentResponseDto> commentServiceDtos = extendedService.readCommentsByNewsId(id);
        List<CommentResponseDto> commentDtos = new ArrayList<>();
        for (ServiceCommentResponseDto comment : commentServiceDtos) {
            commentDtos.add(mapper.mapServiceCommentResponseDto(comment));
        }
        return commentDtos;
    }

    @GetMapping("/with-params")
    public List<NewsResponseDto> readNewsByParams(
            @RequestParam(required = false) List<Long> tagsIds,
            @RequestParam(required = false) String tagName,
            @RequestParam(required = false) String authorName,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content) {
        List<NewsResponseDto> newsResponseDtoList = new ArrayList<>();
        for (ServiceNewsResponseDto serviceDto : extendedService.readNewsByParams(
                tagsIds, tagName, authorName, title, content)) {
            newsResponseDtoList.add(mapper.mapServiceNewsResponseDto(serviceDto));
        }
        return newsResponseDtoList;
    }
}
