package com.mjc.school.controller.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.ServiceAuthorRequestDto;
import com.mjc.school.service.dto.ServiceAuthorResponseDto;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.delete;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AuthorControllerTest {

    @Autowired
    BaseService<ServiceAuthorRequestDto, ServiceAuthorResponseDto, Long> authorService;

    private final String BASE_URI = "http://localhost:8080";
    private final String REQUEST_MAPPING_URI = "/authors";
    private final String EXPECTED_AUTHOR_NAME = "Brigitte Hunt";
    private final ObjectMapper mapper = new ObjectMapper();
    private Long authorId;

    @BeforeEach
    void setUp() {
        ServiceAuthorRequestDto author = new ServiceAuthorRequestDto(null, EXPECTED_AUTHOR_NAME);
        ServiceAuthorResponseDto createdAuthor = authorService.create(author);
        authorId = createdAuthor.getId();

        mapper.registerModule(new JavaTimeModule());
    }

    @AfterEach
    void tearDown() {
        authorService.deleteById(authorId);
    }

    @Test
    public void getAllAuthorsTest() {
        final int EXPECTED_STATUS_CODE = 200;

        RestAssured.baseURI = BASE_URI + REQUEST_MAPPING_URI;

        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json");

        Response response = httpRequest.request(Method.GET, "");

        String responseBodyAsString = response.asString();

        assertEquals(EXPECTED_STATUS_CODE, response.getStatusCode());
        assertNotNull(responseBodyAsString);
    }

    @Test
    public void getAuthorByIdTest() throws JsonProcessingException {
        final int EXPECTED_STATUS_CODE = 200;

        RestAssured.baseURI = BASE_URI;
        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json");

        Response response = httpRequest.get(REQUEST_MAPPING_URI + "/" + authorId);

        String responseBodyAsString = response.asString();

        ServiceAuthorResponseDto author = mapper.readValue(responseBodyAsString, ServiceAuthorResponseDto.class);

        assertEquals(EXPECTED_STATUS_CODE, response.getStatusCode());
        assertEquals(authorId, author.getId());
        assertEquals(EXPECTED_AUTHOR_NAME, author.getName());
    }

    @Test
    public void createAuthorTest() throws JsonProcessingException {
        final int EXPECTED_STATUS_CODE = 201;
        final String EXPECTED_NAME_OF_NEWLY_CREATED_AUTHOR = "Noel Holmes";
        RestAssured.baseURI = BASE_URI;

        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json");

        ServiceAuthorRequestDto author = new ServiceAuthorRequestDto(null, EXPECTED_NAME_OF_NEWLY_CREATED_AUTHOR);

        String authorString = mapper.writeValueAsString(author);

        Response response = httpRequest.body(authorString).post(REQUEST_MAPPING_URI);

        String responseBodyAsString = response.asString();

        ServiceAuthorResponseDto createdAuthor = mapper.readValue(responseBodyAsString, ServiceAuthorResponseDto.class);

        assertEquals(EXPECTED_STATUS_CODE, response.getStatusCode());
        assertNotNull(createdAuthor.getId());
        assertEquals(EXPECTED_NAME_OF_NEWLY_CREATED_AUTHOR, createdAuthor.getName());

        authorService.deleteById(createdAuthor.getId());
    }

    @Test
    public void updateAuthorTest() throws JsonProcessingException {
        final int EXPECTED_STATUS_CODE = 200;
        final String EXPECTED_AUTHOR_NAME_AFTER_UPDATE = "Updated Author";

        RestAssured.baseURI = BASE_URI;

        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json");

        ServiceAuthorRequestDto authorWithNewContent = new ServiceAuthorRequestDto(
                authorId, EXPECTED_AUTHOR_NAME_AFTER_UPDATE);

        String authorWithNewContentAsJson = mapper.writeValueAsString(authorWithNewContent);
        Response response = httpRequest.body(authorWithNewContentAsJson)
                .patch(REQUEST_MAPPING_URI + "/" + authorId);
        String responseBodyAsString = response.asString();
        ServiceAuthorResponseDto updatedAuthor = mapper.readValue(responseBodyAsString, ServiceAuthorResponseDto.class);

        assertEquals(EXPECTED_STATUS_CODE, response.getStatusCode());
        assertEquals(authorId, updatedAuthor.getId());
        assertEquals(EXPECTED_AUTHOR_NAME_AFTER_UPDATE, updatedAuthor.getName());
    }

    @Test
    public void deleteAuthorTest() {
        final int EXPECTED_STATUS_CODE = 204;

        RestAssured.baseURI = BASE_URI;

        ServiceAuthorResponseDto createdAuthor = authorService.create(new ServiceAuthorRequestDto(null, EXPECTED_AUTHOR_NAME));
        Response response = delete(REQUEST_MAPPING_URI + "/" + createdAuthor.getId());

        assertEquals(EXPECTED_STATUS_CODE, response.getStatusCode());
    }
}