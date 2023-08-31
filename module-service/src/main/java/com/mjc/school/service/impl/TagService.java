package com.mjc.school.service.impl;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.model.News;
import com.mjc.school.repository.model.Tag;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.ServiceTagDto;
import com.mjc.school.service.mapper.TagMapper;
import com.mjc.school.service.validator.annotations.ValidateInput;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService implements BaseService<ServiceTagDto, ServiceTagDto, Long> {

    private final TagMapper mapper = new TagMapper();

    private final BaseRepository<Tag, Long> tagRepository;

    private final BaseRepository<News, Long> newsRepository;


    public TagService(BaseRepository<Author, Long> authorRepository,
                      BaseRepository<News, Long> newsRepository,
                      BaseRepository<Tag, Long> tagRepository) {
        this.tagRepository = tagRepository;
        this.newsRepository = newsRepository;

    }

    @Override
    public List<ServiceTagDto> readAll() {
        List<ServiceTagDto> tagDtoList = new ArrayList<>();
        for (Tag tag : tagRepository.readAll()) {
            tagDtoList.add(mapper.mapModelToServiceDto(tag));
        }
        return tagDtoList;
    }

    @Override
    public List<ServiceTagDto> readAll(Integer pageNumber, Integer pageSize, String sortBy) {
        List<ServiceTagDto> tagDtoList = new ArrayList<>();
        for (Tag tag : tagRepository.readAll(pageNumber, pageSize, sortBy)) {
            tagDtoList.add(mapper.mapModelToServiceDto(tag));
        }
        return tagDtoList;
    }

    @Override
    @ValidateInput
    public ServiceTagDto readById(Long id) {
        Tag tag = tagRepository.readById(id).get();
        return mapper.mapModelToServiceDto(tag);
    }

    @Override
    @ValidateInput
    public ServiceTagDto create(ServiceTagDto serviceTagDto) {
        return mapper.mapModelToServiceDto(tagRepository.create(
                mapper.mapServiceDtoToModel(serviceTagDto)
        ));
    }

    @Override
    @ValidateInput
    public ServiceTagDto update(ServiceTagDto serviceTagDto) {
        return mapper.mapModelToServiceDto(
                tagRepository.update(
                        mapper.mapServiceDtoToModel(serviceTagDto)
                ));
    }

    @Override
    @ValidateInput
    public boolean deleteById(Long id) {
        return tagRepository.deleteById(id);
    }
}
