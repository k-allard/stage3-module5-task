package com.mjc.school.service.mapper;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.model.News;
import com.mjc.school.repository.model.Tag;
import com.mjc.school.service.dto.ServiceNewsRequestDto;
import com.mjc.school.service.dto.ServiceNewsResponseDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NewsMapper {

    private final BaseRepository<Author, Long> authorRepository;
    private final BaseRepository<Tag, Long> tagRepository;
    private final ModelMapper mapper = new ModelMapper();

    public NewsMapper(BaseRepository<Author, Long> authorRepository,
                      BaseRepository<Tag, Long> tagRepository) {
        this.authorRepository = authorRepository;
        this.tagRepository = tagRepository;
    }

    public ServiceNewsResponseDto mapModelToResponseDto(News newsModel) {
        return mapper.map(newsModel, ServiceNewsResponseDto.class);
    }

    public News mapResponseDtoToModel(ServiceNewsResponseDto news) {
        News newsModel = mapper.map(news, News.class);
        newsModel.setAuthor(authorRepository.readById(news.getAuthorId()).orElse(null));
        if (news.getNewsTagsIds() != null) {
            List<Tag> tagsModels = new ArrayList<>();
            for (Long tagId : news.getNewsTagsIds()) {
                tagsModels.add(tagRepository.readById(tagId).get());
            }
            newsModel.setNewsTags(tagsModels);
        }
        return newsModel;
    }

    public News mapRequestDtoToModel(ServiceNewsRequestDto news) {
        News newsModel = mapper.map(news, News.class);
        newsModel.setAuthor(authorRepository.readById(news.getAuthorId()).orElse(null));
        if (news.getNewsTagsIds() != null) {
            List<Tag> tagsModels = new ArrayList<>();
            for (Long tagId : news.getNewsTagsIds()) {
                tagsModels.add(tagRepository.readById(tagId).get());
            }
            newsModel.setNewsTags(tagsModels);
        }
        return newsModel;
    }
}
