package com.mjc.school.service.validator;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.model.Tag;
import com.mjc.school.service.dto.ServiceTagDto;
import com.mjc.school.service.exceptions.NotFoundException;
import com.mjc.school.service.exceptions.ValidatorException;
import org.springframework.stereotype.Component;

import static com.mjc.school.service.exceptions.ExceptionsCodes.TAG_ID_DOES_NOT_EXIST;
import static com.mjc.school.service.exceptions.ExceptionsCodes.VALIDATE_NEGATIVE_OR_NULL_NUMBER;
import static com.mjc.school.service.exceptions.ExceptionsCodes.VALIDATE_STRING_LENGTH;

@Component
public class TagDtoValidator {

    private final BaseRepository<Tag, Long> tagRepository;

    public TagDtoValidator(BaseRepository<Tag, Long> repository) {
        tagRepository = repository;
    }

    public void validateTagDTO(ServiceTagDto dto) {
        checkName(dto.getName());
    }

    public void validateTagId(Long id) {
        if (id == null || id < 1) {
            throw new ValidatorException(
                    String.format(VALIDATE_NEGATIVE_OR_NULL_NUMBER.getMessage(), "Tag id", "Tag id", id));
        }
        if (!tagRepository.existById(id)) {
            throw new NotFoundException(String.format(TAG_ID_DOES_NOT_EXIST.getMessage(), id));
        }
    }

    private void checkName(String name) {
        if (name == null || name.length() < 3 || name.length() > 15) {
            throw new ValidatorException(String.format(VALIDATE_STRING_LENGTH.getMessage(),
                    "Tag name", 3, 15, "Tag name", name));
        }
    }
}
