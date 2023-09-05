package com.mjc.school.controller.impl;

import com.mjc.school.controller.BaseController;
import com.mjc.school.controller.dto.TagDto;
import com.mjc.school.controller.mapper.ServiceToWebDTOMapper;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.ServiceTagDto;
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
@RequestMapping(value = ("/tags"))
public class TagController implements BaseController<TagDto, TagDto, Long> {
    private final BaseService<ServiceTagDto, ServiceTagDto, Long> tagService;
    private final ServiceToWebDTOMapper mapper = new ServiceToWebDTOMapper();

    public TagController(
            @Qualifier("tagService")
            BaseService<ServiceTagDto, ServiceTagDto, Long> tagService
    ) {
        this.tagService = tagService;
    }

    @Override
    @ApiOperation("Read all tags")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")
    })
    @GetMapping
    public List<TagDto> readAll(@RequestParam(required = false) Integer pageNumber,
                                @RequestParam(required = false, defaultValue = "3") Integer pageSize,
                                @RequestParam(required = false) String sortBy) {
        List<TagDto> authorResponseDtoList = new ArrayList<>();
        for (ServiceTagDto serviceTagDto : tagService.readAll(pageNumber, pageSize, sortBy)) {
            authorResponseDtoList.add(mapper.mapServiceTagDto(serviceTagDto));
        }
        return authorResponseDtoList;
    }

    @ApiOperation("Read tag by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")
    })
    @GetMapping("/{id}")
    public TagDto readById(@PathVariable Long id) {
        return mapper.mapServiceTagDto(tagService.readById(id));
    }

    @PostMapping
    @ApiOperation("Create tag")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto create(@RequestBody TagDto tagDto) {
        return mapper.mapServiceTagDto(
                tagService.create(mapper.mapTagToServiceDto(tagDto)));
    }

    @PatchMapping("/{id}")
    @ApiOperation("Update tag")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    public TagDto update(@PathVariable Long id,
                         @RequestBody TagDto dtoRequest) {
        return mapper.mapServiceTagDto(
                tagService.update(mapper.mapTagToServiceDto(dtoRequest)));
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete tag by id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        tagService.deleteById(id);
    }
}
