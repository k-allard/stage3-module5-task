package com.mjc.school.service.validator;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.model.Comment;
import com.mjc.school.repository.model.News;
import com.mjc.school.service.dto.ServiceCommentRequestDto;
import com.mjc.school.service.exceptions.NotFoundException;
import com.mjc.school.service.exceptions.ValidatorException;
import org.springframework.stereotype.Component;

import static com.mjc.school.service.exceptions.ExceptionsCodes.COMMENT_ID_DOES_NOT_EXIST;
import static com.mjc.school.service.exceptions.ExceptionsCodes.NEWS_ID_DOES_NOT_EXIST;
import static com.mjc.school.service.exceptions.ExceptionsCodes.VALIDATE_NEGATIVE_OR_NULL_NUMBER;
import static com.mjc.school.service.exceptions.ExceptionsCodes.VALIDATE_STRING_LENGTH;

@Component
public class CommentRequestDtoValidator {

    private final BaseRepository<Comment, Long> commentRepository;

    private final BaseRepository<News, Long> newsRepository;

    public CommentRequestDtoValidator(BaseRepository<Comment, Long> commentRepository, BaseRepository<News, Long> newsRepository) {
        this.commentRepository = commentRepository;
        this.newsRepository = newsRepository;
    }

    public void validateCommentDTO(ServiceCommentRequestDto dto) {
        validateNewsId(dto.getNewsId());
        checkContent(dto.getContent());
    }

    public void validateCommentId(Long id) {
        if (id == null || id < 1) {
            throw new ValidatorException(
                    String.format(VALIDATE_NEGATIVE_OR_NULL_NUMBER.getMessage(), "Comment id", "Comment id", id));
        }
        if (!commentRepository.existById(id)) {
            throw new NotFoundException(String.format(COMMENT_ID_DOES_NOT_EXIST.getMessage(), id));
        }
    }

    private void validateNewsId(Long id) {
        if (id == null || id < 1) {
            throw new ValidatorException(
                    String.format(VALIDATE_NEGATIVE_OR_NULL_NUMBER.getMessage(), "News id", "News id", id));
        }
        if (!newsRepository.existById(id)) {
            throw new NotFoundException(String.format(NEWS_ID_DOES_NOT_EXIST.getMessage(), id));
        }
    }

    private void checkContent(String content) {
        if (content == null || content.length() < 5 || content.length() > 255) {
            throw new ValidatorException(String.format(VALIDATE_STRING_LENGTH.getMessage(),
                    "Comment content", 5, 255, "Comment content", content));
        }
    }
}
