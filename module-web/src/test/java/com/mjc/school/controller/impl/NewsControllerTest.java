package com.mjc.school.controller.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.ServiceAuthorRequestDto;
import com.mjc.school.service.dto.ServiceAuthorResponseDto;
import com.mjc.school.service.dto.ServiceNewsRequestDto;
import com.mjc.school.service.dto.ServiceNewsResponseDto;
import com.mjc.school.service.impl.NewsService;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.Matchers.notNullValue;
import static io.restassured.RestAssured.get;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class NewsControllerTest {

    @Autowired
    BaseService<ServiceNewsRequestDto, ServiceNewsResponseDto, Long> newsService;

    private final String BASE_URI = "http://localhost:8080";
    private final String REQUEST_MAPPING_URI = "/news";
    private final String EXPECTED_NEWS_CONTENT = "Financial News";
    private final ObjectMapper mapper = new ObjectMapper();
    private Long newsID;

    @BeforeEach
    void setUp() {
        ServiceNewsRequestDto news = new ServiceNewsRequestDto(EXPECTED_NEWS_CONTENT);
        ServiceNewsResponseDto createdNews = newsService.create(news);
        newsID = createdNews.getId();

        mapper.registerModule(new JavaTimeModule());
    }
    @AfterEach
    void tearDown() {
        newsService.deleteById(newsID);
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

        Response response = httpRequest.get(REQUEST_MAPPING_URI + "/" + newsID);

        String responseBodyAsString = response.asString();

        ServiceNewsResponseDto news = mapper.readValue(responseBodyAsString, ServiceNewsResponseDto.class);

        assertEquals(EXPECTED_STATUS_CODE, response.getStatusCode());
        assertEquals(newsID, news.getId());
        assertEquals(EXPECTED_NEWS_CONTENT, news.getContent());
    }

    @Test
    public void createNewsTest() throws JsonProcessingException {
        final int EXPECTED_STATUS_CODE = 201;
        final String EXPECTED_CONTENT_OF_NEWLY_CREATED_NEWS = "Environmental News";
        RestAssured.baseURI = BASE_URI;

        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json");

        ServiceNewsRequestDto news = new ServiceNewsRequestDto(EXPECTED_CONTENT_OF_NEWLY_CREATED_NEWS);

        String newsString = mapper.writeValueAsString(news);

        Response response = httpRequest.body(newsString).post(REQUEST_MAPPING_URI);

        String responseBodyAsString = response.asString();

        ServiceNewsResponseDto createdNews = mapper.readValue(responseBodyAsString, ServiceNewsResponseDto.class);

        assertEquals(EXPECTED_STATUS_CODE, response.getStatusCode());
        assertNotNull(createdNews.getId());
        assertEquals(EXPECTED_CONTENT_OF_NEWLY_CREATED_NEWS, createdNews.getContent());

        newsService.deleteById(createdNews.getId());
    }

    @Test
    public void updateNewsTest() throws JsonProcessingException {
        final int EXPECTED_STATUS_CODE = 200;
        final String EXPECTED_NEWS_CONTENT_AFTER_UPDATE = "Updated Financial News";

        RestAssured.baseURI = BASE_URI;

        RequestSpecification httpRequest = RestAssured.given()
                .header("Content-Type", "application/json");

        ServiceNewsRequestDto newsWithNewContent = new ServiceNewsRequestDto(
                newsID, EXPECTED_NEWS_CONTENT_AFTER_UPDATE);

        String newsWithNewContentAsJson = mapper.writeValueAsString(newsWithNewContent);
        Response response = httpRequest.body(newsWithNewContentAsJson).patch(REQUEST_MAPPING_URI + "/" + newsID);
        String responseBodyAsString = response.asString();
        ServiceNewsResponseDto updatedNews = mapper.readValue(responseBodyAsString, ServiceNewsResponseDto.class);

        assertEquals(EXPECTED_STATUS_CODE, response.getStatusCode());
        assertEquals(newsID, updatedNews.getId());
        assertEquals(EXPECTED_NEWS_CONTENT_AFTER_UPDATE, updatedNews.getContent());
    }

    @Test
    public void deleteNewsTest() {
        final int EXPECTED_STATUS_CODE = 204;

        RestAssured.baseURI = BASE_URI;
        RequestSpecification httpRequest = RestAssured.given()
                ; //TODO check

        ServiceNewsResponseDto createdNews = newsService.create(new ServiceNewsRequestDto(EXPECTED_NEWS_CONTENT));
        Response response = httpRequest.delete(REQUEST_MAPPING_URI + "/" + createdNews.getId());

        assertEquals(EXPECTED_STATUS_CODE, response.getStatusCode());
    }
}