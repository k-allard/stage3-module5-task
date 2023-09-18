package com.mjc.school.controller.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.ServiceTagDto;
import com.mjc.school.service.dto.ServiceNewsRequestDto;
import com.mjc.school.service.dto.ServiceNewsResponseDto;
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
public class TagControllerTest {

    @Autowired
    BaseService<ServiceNewsRequestDto, ServiceNewsResponseDto, Long> newsService;

    @Autowired
    BaseService<ServiceTagDto, ServiceTagDto, Long> commentService;

    @Autowired
    BaseService<ServiceTagDto, ServiceTagDto, Long> tagService;

    private final String BASE_URI = "http://localhost:8080";
    private final String REQUEST_MAPPING_URI = "/tags";
    private final String EXPECTED_TAG_NAME = "Politics";
    private final ObjectMapper mapper = new ObjectMapper();
    private Long commentId;
    private Long newsId;
    private Long tagId;

    @BeforeEach
    void setUp() {
        String EXPECTED_NEWS_CONTENT = "Financial News";
        ServiceNewsResponseDto newsResponseDto = newsService.create(
                new ServiceNewsRequestDto(null, EXPECTED_NEWS_CONTENT));
        newsId = newsResponseDto.getId();
        ServiceTagDto commentRequestDto = new ServiceTagDto(
                null, EXPECTED_TAG_NAME);
        ServiceTagDto createdComment = tagService.create(commentRequestDto);
        tagId = createdComment.getId();

        mapper.registerModule(new JavaTimeModule());
    }

    @AfterEach
    void tearDown() {
//        commentService.deleteById(commentId);
//        newsService.deleteById(newsId);
        tagService.deleteById(tagId);
    }

    @Test
    public void getAllNewsTest() {
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
    public void getNewsByIdTest() throws JsonProcessingException {
        final int EXPECTED_STATUS_CODE = 200;

        RestAssured.baseURI = BASE_URI;
        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json");

        Response response = httpRequest.get(REQUEST_MAPPING_URI + "/" + tagId);

        String responseBodyAsString = response.asString();

        ServiceTagDto tagDto = mapper.readValue(responseBodyAsString, ServiceTagDto.class);

        assertEquals(EXPECTED_STATUS_CODE, response.getStatusCode());
        assertEquals(tagId, tagDto.getId());
        assertEquals(EXPECTED_TAG_NAME, tagDto.getName());
    }

    @Test
    public void createNewsTest() throws JsonProcessingException {
        final int EXPECTED_STATUS_CODE = 201;
        RestAssured.baseURI = BASE_URI;

        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json");

        ServiceTagDto tagDto = new ServiceTagDto(null, EXPECTED_TAG_NAME);

        String tagString = mapper.writeValueAsString(tagDto);

        Response response = httpRequest.body(tagString).post(REQUEST_MAPPING_URI);

        ServiceTagDto tagResponseDto = mapper.readValue(
                response.asString(), ServiceTagDto.class);

        assertEquals(EXPECTED_STATUS_CODE, response.getStatusCode());
        assertNotNull(tagResponseDto.getId());
        assertEquals(EXPECTED_TAG_NAME, tagResponseDto.getName());

        tagService.deleteById(tagResponseDto.getId());
    }

    @Test
    public void updateNewsTest() throws JsonProcessingException {
        final int EXPECTED_STATUS_CODE = 200;
        final String EXPECTED_TAG_NAME_AFTER_UPDATE = "Updated Tag";

        RestAssured.baseURI = BASE_URI;

        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json");

        ServiceTagDto commentWithNewContent = new ServiceTagDto(
                tagId, EXPECTED_TAG_NAME_AFTER_UPDATE);

        String newsWithNewContentAsJson = mapper.writeValueAsString(commentWithNewContent);
        Response response = httpRequest.body(newsWithNewContentAsJson)
                .patch(REQUEST_MAPPING_URI + "/" + tagId);
        String responseBodyAsString = response.asString();
        ServiceTagDto updatedComment = mapper.readValue(
                responseBodyAsString, ServiceTagDto.class);

        assertEquals(EXPECTED_STATUS_CODE, response.getStatusCode());
        assertEquals(tagId, updatedComment.getId());
        assertEquals(EXPECTED_TAG_NAME_AFTER_UPDATE, updatedComment.getName());
    }

    @Test
    public void deleteNewsTest() {
        final int EXPECTED_STATUS_CODE = 204;

        RestAssured.baseURI = BASE_URI;

        ServiceTagDto tagDto = tagService.create(
                new ServiceTagDto(null, EXPECTED_TAG_NAME));
        Response response = delete(REQUEST_MAPPING_URI + "/" + tagDto.getId());

        assertEquals(EXPECTED_STATUS_CODE, response.getStatusCode());
    }
}