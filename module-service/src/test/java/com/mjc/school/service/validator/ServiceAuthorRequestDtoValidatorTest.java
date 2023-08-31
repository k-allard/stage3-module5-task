package com.mjc.school.service.validator;

import com.mjc.school.repository.impl.AuthorRepository;
import com.mjc.school.service.dto.ServiceAuthorRequestDto;
import com.mjc.school.service.exceptions.NotFoundException;
import com.mjc.school.service.exceptions.ValidatorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class ServiceAuthorRequestDtoValidatorTest {

    private static final long VALID_AUTHOR_ID = 2L;
    private static final Long INVALID_AUTHOR_ID = null;
    private static final String VALID_AUTHOR_NAME = "A. Dumbledore";
    private static final String INVALID_AUTHOR_NAME = "Albus Percival Wulfric Brian Dumbledore";
    private static final long NONEXISTENT_AUTHOR_ID = 66L;

    private AuthorRequestDtoValidator validator;

    @BeforeEach
    void init(@Mock AuthorRepository authorRepository) {
        validator = new AuthorRequestDtoValidator(authorRepository);
        lenient().when(authorRepository.existById(VALID_AUTHOR_ID))
                .thenReturn(true);
    }

    @Test
    @DisplayName("validateAuthorId() with existent id - OK")
    public void validateValidId() {
        assertDoesNotThrow(() ->
                validator.validateAuthorId(VALID_AUTHOR_ID));
    }

    @Test
    @DisplayName("validateAuthorDTO() with valid AuthorRequestDto - OK")
    void validateValidDTO() {
        assertDoesNotThrow(() ->
                validator.validateAuthorDTO(
                        new ServiceAuthorRequestDto(VALID_AUTHOR_ID, VALID_AUTHOR_NAME)));
    }


    @Test
    @DisplayName("validateAuthorDTO() with invalid name fails")
    void validateInvalidName() {
        ValidatorException thrown = assertThrows(ValidatorException.class, () ->
                validator.validateAuthorDTO(
                        new ServiceAuthorRequestDto(null, INVALID_AUTHOR_NAME)));
        assertTrue(thrown.getMessage().contains("Author name can not be"));
    }

    @Test
    @DisplayName("validateAuthorId() with non-existent author id fails")
    void validateNonexistentId() {
        NotFoundException thrown = assertThrows(NotFoundException.class, () ->
                validator.validateAuthorId(NONEXISTENT_AUTHOR_ID));
        assertTrue(thrown.getMessage().contains("Author Id does not exist"));
    }

    @Test
    @DisplayName("validateAuthorId() with invalid author id fails")
    void validateInvalidId() {
        ValidatorException thrown = assertThrows(ValidatorException.class, () ->
                validator.validateAuthorId(INVALID_AUTHOR_ID));
        assertTrue(thrown.getMessage().contains("can not be null or less than 1"));
    }
}
