package com.mjc.school.service.validator;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.model.News;
import com.mjc.school.service.dto.ServiceNewsRequestDto;
import com.mjc.school.service.exceptions.NotFoundException;
import com.mjc.school.service.exceptions.ValidatorException;
import org.springframework.stereotype.Component;

import static com.mjc.school.service.exceptions.ExceptionsCodes.AUTHOR_ID_DOES_NOT_EXIST;
import static com.mjc.school.service.exceptions.ExceptionsCodes.NEWS_ID_DOES_NOT_EXIST;
import static com.mjc.school.service.exceptions.ExceptionsCodes.VALIDATE_NEGATIVE_OR_NULL_NUMBER;
import static com.mjc.school.service.exceptions.ExceptionsCodes.VALIDATE_STRING_LENGTH;

@Component
public class NewsRequestDtoValidator {

    private final BaseRepository<Author, Long> authorRepository;
    private final BaseRepository<News, Long> newsRepository;

    public NewsRequestDtoValidator(BaseRepository<Author, Long> authorRepository,
                                   BaseRepository<News, Long> newsRepository) {
        this.authorRepository = authorRepository;
        this.newsRepository = newsRepository;
    }


    public void validateNewsDTORequest(ServiceNewsRequestDto dto) {
        if (dto.getTitle() != null)
            checkNewsTitle(dto.getTitle());
        if (dto.getContent() != null)
            checkNewsContent(dto.getContent());
    }

    public void validateNewsId(Long id) {
        if (id == null || id < 1) {
            throw new ValidatorException(
                    String.format(VALIDATE_NEGATIVE_OR_NULL_NUMBER.getMessage(),
                            "News id", "News id", id));
        }
        if (!newsRepository.existById(id)) {
            throw new NotFoundException(
                    String.format(NEWS_ID_DOES_NOT_EXIST.getMessage(), id));
        }
    }

    public void validateAuthorId(Long id) {
        if (id == null)
            return;
        if (id < 1) {
            throw new ValidatorException(
                    String.format(VALIDATE_NEGATIVE_OR_NULL_NUMBER.getMessage(), "Author id", "Author id", id));
        }
        if (!authorRepository.existById(id)) {
            throw new NotFoundException(String.format(AUTHOR_ID_DOES_NOT_EXIST.getMessage(), id));
        }
    }

    private void checkNewsTitle(String title) {
        if (title == null || title.length() < 5 || title.length() > 30) {
            throw new ValidatorException(
                    String.format(VALIDATE_STRING_LENGTH.getMessage(),
                            "News title", 5, 30, "News title", title)
            );
        }
    }

    private void checkNewsContent(String content) {
        if (content == null || content.length() < 5 || content.length() > 255) {
            throw new ValidatorException(
                    String.format(VALIDATE_STRING_LENGTH.getMessage(),
                            "News content", 5, 255, "News content", content)
            );
        }
    }
}
