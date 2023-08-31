package com.mjc.school.service.mapper;

import com.mjc.school.repository.model.Author;
import com.mjc.school.service.dto.ServiceAuthorRequestDto;
import com.mjc.school.service.dto.ServiceAuthorResponseDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {
    private final ModelMapper mapper = new ModelMapper();

    public ServiceAuthorResponseDto mapModelToResponseDto(Author author) {
        return mapper.map(author, ServiceAuthorResponseDto.class);
    }

    public Author mapResponseDtoToModel(ServiceAuthorResponseDto author) {
        return mapper.map(author, Author.class);
    }

    public Author mapRequestDtoToModel(ServiceAuthorRequestDto author) {
        return mapper.map(author, Author.class);
    }
}
