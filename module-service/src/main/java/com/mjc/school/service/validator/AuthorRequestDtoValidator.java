package com.mjc.school.service.validator;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.model.Author;
import com.mjc.school.service.dto.ServiceAuthorRequestDto;
import com.mjc.school.service.exceptions.NotFoundException;
import com.mjc.school.service.exceptions.ValidatorException;
import org.springframework.stereotype.Component;

import static com.mjc.school.service.exceptions.ExceptionsCodes.AUTHOR_ID_DOES_NOT_EXIST;
import static com.mjc.school.service.exceptions.ExceptionsCodes.VALIDATE_NEGATIVE_OR_NULL_NUMBER;
import static com.mjc.school.service.exceptions.ExceptionsCodes.VALIDATE_STRING_LENGTH;

@Component
public class AuthorRequestDtoValidator {

    private final BaseRepository<Author, Long> authorRepository;

    public AuthorRequestDtoValidator(BaseRepository<Author, Long> repository) {
        authorRepository = repository;
    }

    public void validateAuthorDTO(ServiceAuthorRequestDto dto) {
        checkName(dto.getName());
    }

    public void validateAuthorId(Long id) {
        if (id == null || id < 1) {
            throw new ValidatorException(
                    String.format(VALIDATE_NEGATIVE_OR_NULL_NUMBER.getMessage(), "Author id", "Author id", id));
        }
        if (!authorRepository.existById(id)) {
            throw new NotFoundException(String.format(AUTHOR_ID_DOES_NOT_EXIST.getMessage(), id));
        }
    }

    private void checkName(String name) {
        if (name == null || name.length() < 3 || name.length() > 15) {
            throw new ValidatorException(String.format(VALIDATE_STRING_LENGTH.getMessage(),
                    "Author name", 3, 15, "Author name", name));
        }
    }
}
