package com.mjc.school.controller.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.ServiceCommentRequestDto;
import com.mjc.school.service.dto.ServiceCommentResponseDto;
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
public class CommentControllerTest {

    @Autowired
    BaseService<ServiceNewsRequestDto, ServiceNewsResponseDto, Long> newsService;

    @Autowired
    BaseService<ServiceCommentRequestDto, ServiceCommentResponseDto, Long> commentService;

    private final String BASE_URI = "http://localhost:8080";
    private final String REQUEST_MAPPING_URI = "/comments";
    private final String EXPECTED_COMMENT_CONTENT = "Great News!";
    private final ObjectMapper mapper = new ObjectMapper();
    private Long commentId;
    private Long newsId;

    @BeforeEach
    void setUp() {
        String EXPECTED_NEWS_CONTENT = "Financial News";
        ServiceNewsResponseDto newsResponseDto = newsService.create(
                new ServiceNewsRequestDto(null, EXPECTED_NEWS_CONTENT));
        newsId = newsResponseDto.getId();
        ServiceCommentRequestDto commentRequestDto = new ServiceCommentRequestDto(
                null, EXPECTED_COMMENT_CONTENT, newsId);
        ServiceCommentResponseDto createdComment = commentService.create(commentRequestDto);
        commentId = createdComment.getId();

        mapper.registerModule(new JavaTimeModule());
    }

    @AfterEach
    void tearDown() {
        commentService.deleteById(commentId);
        newsService.deleteById(newsId);
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

        Response response = httpRequest.get(REQUEST_MAPPING_URI + "/" + commentId);

        String responseBodyAsString = response.asString();

        ServiceCommentResponseDto news = mapper.readValue(responseBodyAsString, ServiceCommentResponseDto.class);

        assertEquals(EXPECTED_STATUS_CODE, response.getStatusCode());
        assertEquals(commentId, news.getId());
        assertEquals(EXPECTED_COMMENT_CONTENT, news.getContent());
    }

    @Test
    public void createNewsTest() throws JsonProcessingException {
        final int EXPECTED_STATUS_CODE = 201;
        RestAssured.baseURI = BASE_URI;

        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json");

        ServiceCommentRequestDto comment = new ServiceCommentRequestDto(null, EXPECTED_COMMENT_CONTENT, newsId);

        String commentString = mapper.writeValueAsString(comment);

        Response response = httpRequest.body(commentString).post(REQUEST_MAPPING_URI);

        ServiceCommentResponseDto commentResponseDto = mapper.readValue(
                response.asString(), ServiceCommentResponseDto.class);

        assertEquals(EXPECTED_STATUS_CODE, response.getStatusCode());
        assertNotNull(commentResponseDto.getId());
        assertEquals(EXPECTED_COMMENT_CONTENT, commentResponseDto.getContent());
        assertEquals(newsId, commentResponseDto.getNewsId());

        commentService.deleteById(commentResponseDto.getId());
    }

    @Test
    public void updateNewsTest() throws JsonProcessingException {
        final int EXPECTED_STATUS_CODE = 200;
        final String EXPECTED_COMMENT_CONTENT_AFTER_UPDATE = "Updated Financial News";

        RestAssured.baseURI = BASE_URI;

        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json");

        ServiceCommentRequestDto commentWithNewContent = new ServiceCommentRequestDto(
                commentId, EXPECTED_COMMENT_CONTENT_AFTER_UPDATE, newsId);

        String newsWithNewContentAsJson = mapper.writeValueAsString(commentWithNewContent);
        Response response = httpRequest.body(newsWithNewContentAsJson)
                .patch(REQUEST_MAPPING_URI + "/" + commentId);
        String responseBodyAsString = response.asString();
        ServiceCommentResponseDto updatedComment = mapper.readValue(
                responseBodyAsString, ServiceCommentResponseDto.class);

        assertEquals(EXPECTED_STATUS_CODE, response.getStatusCode());
        assertEquals(commentId, updatedComment.getId());
        assertEquals(EXPECTED_COMMENT_CONTENT_AFTER_UPDATE, updatedComment.getContent());
    }

    @Test
    public void deleteNewsTest() {
        final int EXPECTED_STATUS_CODE = 204;

        RestAssured.baseURI = BASE_URI;

        ServiceCommentResponseDto commentResponseDto = commentService.create(
                new ServiceCommentRequestDto(null, EXPECTED_COMMENT_CONTENT, newsId));
        Response response = delete(REQUEST_MAPPING_URI + "/" + commentResponseDto.getId());

        assertEquals(EXPECTED_STATUS_CODE, response.getStatusCode());
    }
}