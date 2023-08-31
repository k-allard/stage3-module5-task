package com.mjc.school.service;

import com.mjc.school.repository.impl.AuthorRepository;
import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.model.News;
import com.mjc.school.service.dto.ServiceAuthorRequestDto;
import com.mjc.school.service.dto.ServiceAuthorResponseDto;
import com.mjc.school.service.impl.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {
    private static final long VALID_AUTHOR_ID = 3L;
    private static final String VALID_AUTHOR_NAME = "Valid Name";
    private final List<Author> authorList = new ArrayList<>();
    List<News> newsList = new ArrayList<>();

    @Mock
    private AuthorRepository authorRepository;
    @InjectMocks
    private AuthorService authorService;

    @BeforeEach
    void init() {
        newsList.add(new News(1L));
        newsList.add(new News(2L));
        newsList.add(new News(3L));

        authorList.add(new Author(1L, "Agatha Christie", LocalDateTime.now(), LocalDateTime.now(), newsList));
        authorList.add(new Author(2L, "Husna Ahmad", LocalDateTime.now(), LocalDateTime.now(), newsList));
        authorList.add(new Author(3L, "Svenja Adolphs", LocalDateTime.now(), LocalDateTime.now(), newsList));
    }

    @Test
    @DisplayName("readAll() returns initial list of authors")
    void readAll() {
        Mockito.when(authorRepository.readAll())
                .thenReturn(authorList);
        List<ServiceAuthorResponseDto> list = authorService.readAll();
        assertEquals(authorList.size(), list.size());
    }

    @Test
    @DisplayName("readById() returns correct author")
    void getAuthorByValidId() {
        Mockito.when(authorRepository.readById(VALID_AUTHOR_ID))
                .thenReturn(Optional.ofNullable(
                        authorList.get(authorList.indexOf(new Author(VALID_AUTHOR_ID))))
                );
        ServiceAuthorResponseDto author = authorService.readById(VALID_AUTHOR_ID);
        assertEquals(VALID_AUTHOR_ID, author.getId());
    }


    @Test
    @DisplayName("createAuthor() returns new author")
    void createValidAuthorAndCheckResponse() {
        Mockito
                .when(
                        authorRepository.create(any(Author.class)))
                .thenReturn(
                        new Author(6L, VALID_AUTHOR_NAME, LocalDateTime.now(),
                                LocalDateTime.now(), newsList));


        ServiceAuthorResponseDto response = authorService.create(
                new ServiceAuthorRequestDto(null, VALID_AUTHOR_NAME)
        );
        assertEquals(VALID_AUTHOR_NAME, response.getName());
        assertNotNull(response.getId());
        assertNotNull(response.getCreateDate());
        assertNotNull(response.getLastUpdateDate());
    }


    @Test
    @DisplayName("updateAuthor() returns updated author")
    void updateValidAuthorAndCheckResponse() {
        Mockito
                .when(authorRepository.update(argThat(new ValidAuthor())))
                .thenReturn(new Author(
                        VALID_AUTHOR_ID, VALID_AUTHOR_NAME, LocalDateTime.now(),
                        LocalDateTime.now(), newsList));

        ServiceAuthorResponseDto response = authorService.update(
                new ServiceAuthorRequestDto(VALID_AUTHOR_ID, VALID_AUTHOR_NAME)
        );
        assertEquals(VALID_AUTHOR_ID, response.getId());
        assertEquals(VALID_AUTHOR_NAME, response.getName());
    }


    @Test
    @DisplayName("removeAuthor() return true if id existed")
    void removeAuthorWithValidId() {
        Mockito.when(authorRepository.deleteById(VALID_AUTHOR_ID))
                .thenReturn(true);
        assertTrue(authorService.deleteById(VALID_AUTHOR_ID));
    }

    private static class ValidAuthor implements ArgumentMatcher<Author> {
        @Override
        public boolean matches(Author author) {
            return author.getId() == VALID_AUTHOR_ID
                    && Objects.equals(author.getName(), VALID_AUTHOR_NAME);
        }

        public String toString() {
            return "[author with valid id and name]";
        }
    }
}
