package com.mjc.school.controller.impl;

import com.mjc.school.controller.BaseController;
import com.mjc.school.controller.dto.CommentRequestDto;
import com.mjc.school.controller.dto.CommentResponseDto;
import com.mjc.school.controller.mapper.ServiceToWebDTOMapper;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.ServiceCommentRequestDto;
import com.mjc.school.service.dto.ServiceCommentResponseDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@RequestMapping(value = ("/comments"))
public class CommentController implements BaseController<CommentRequestDto, CommentResponseDto, Long> {
    private final BaseService<ServiceCommentRequestDto, ServiceCommentResponseDto, Long> commentService;
    private final ServiceToWebDTOMapper mapper = new ServiceToWebDTOMapper();

    public CommentController(
            @Qualifier("commentService")
            BaseService<ServiceCommentRequestDto, ServiceCommentResponseDto, Long> commentService
    ) {
        this.commentService = commentService;
    }

    @Override
    @ApiOperation("Read all comments")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")
    })
    @GetMapping
    public List<CommentResponseDto> readAll(@RequestParam(required = false) Integer pageNumber,
                                            @RequestParam(required = false, defaultValue = "3") Integer pageSize,
                                            @RequestParam(required = false) String sortBy) {
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for (ServiceCommentResponseDto responseDto : commentService.readAll(pageNumber, pageSize, sortBy)) {
            commentResponseDtoList.add(mapper.mapServiceCommentResponseDto(responseDto));
        }
        return commentResponseDtoList;
    }

    @ApiOperation("Read comment by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")
    })
    @GetMapping("/{id}")
    public CommentResponseDto readById(@PathVariable Long id) {
        return mapper.mapServiceCommentResponseDto(commentService.readById(id));
    }

    @PostMapping
    @ApiOperation("Create comment")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto create(@RequestBody CommentRequestDto dtoRequest) {
        return mapper.mapServiceCommentResponseDto(
                commentService.create(mapper.mapCommentRequestDto(dtoRequest)));
    }

    @PatchMapping("/{id}")
    @ApiOperation("Update comment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    public CommentResponseDto update(@PathVariable Long id,
                                     @RequestBody CommentRequestDto dtoRequest) {
        return mapper.mapServiceCommentResponseDto(
                commentService.update(mapper.mapCommentRequestDto(dtoRequest)));
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete comment by id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        commentService.deleteById(id);
    }
}
