package com.mjc.school.service.mapper;

import com.mjc.school.repository.model.Tag;
import com.mjc.school.service.dto.ServiceTagDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {
    private final ModelMapper mapper = new ModelMapper();

    public ServiceTagDto mapModelToServiceDto(Tag tag) {
        return mapper.map(tag, ServiceTagDto.class);
    }

    public Tag mapServiceDtoToModel(ServiceTagDto tagDto) {
        return mapper.map(tagDto, Tag.class);
    }
}
