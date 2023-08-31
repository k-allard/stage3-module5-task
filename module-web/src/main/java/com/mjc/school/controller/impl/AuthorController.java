package com.mjc.school.controller.impl;

import com.mjc.school.controller.BaseController;
import com.mjc.school.controller.dto.AuthorRequestDto;
import com.mjc.school.controller.dto.AuthorResponseDto;
import com.mjc.school.controller.mapper.ServiceToWebDTOMapper;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.ServiceAuthorRequestDto;
import com.mjc.school.service.dto.ServiceAuthorResponseDto;
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
@RequestMapping(value = ("/authors"))
public class AuthorController implements BaseController<AuthorRequestDto, AuthorResponseDto, Long> {
    private final BaseService<ServiceAuthorRequestDto, ServiceAuthorResponseDto, Long> authorService;
    private final ServiceToWebDTOMapper mapper = new ServiceToWebDTOMapper();

    public AuthorController(
            @Qualifier("authorService")
            BaseService<ServiceAuthorRequestDto, ServiceAuthorResponseDto, Long> authorService
    ) {
        this.authorService = authorService;
    }

    @Override
    @GetMapping
    public List<AuthorResponseDto> readAll(@RequestParam(required = false) Integer pageNumber,
                                           @RequestParam(required = false, defaultValue = "3") Integer pageSize,
                                           @RequestParam(required = false) String sortBy) {
        List<AuthorResponseDto> authorResponseDtoList = new ArrayList<>();
        for (ServiceAuthorResponseDto responseDto : authorService.readAll(pageNumber, pageSize, sortBy)) {
            authorResponseDtoList.add(mapper.mapServiceAuthorResponseDto(responseDto));
        }
        return authorResponseDtoList;
    }

    @GetMapping("/{id}")
    public AuthorResponseDto readById(@PathVariable Long id) {
        return mapper.mapServiceAuthorResponseDto(authorService.readById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorResponseDto create(@RequestBody AuthorRequestDto dtoRequest) {
        return mapper.mapServiceAuthorResponseDto(
                authorService.create(mapper.mapAuthorRequestDto(dtoRequest)));
    }

    @PatchMapping("/{id}")
    public AuthorResponseDto update(@PathVariable Long id,
                                    @RequestBody AuthorRequestDto dtoRequest) {
        return mapper.mapServiceAuthorResponseDto(
                authorService.update(mapper.mapAuthorRequestDto(dtoRequest)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        authorService.deleteById(id);
    }
}
