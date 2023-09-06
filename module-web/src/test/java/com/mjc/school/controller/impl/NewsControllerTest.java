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

//TODO add all tests
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class NewsControllerTest {

    @Autowired
    BaseService<ServiceNewsRequestDto, ServiceNewsResponseDto, Long> newsService;

    @LocalServerPort
    private int port;

//    @Test
//    public void getAllAuthors() {
//        RestAssured.given().port(port).contentType("application/json").get("/authors")
//                .then()
//                .body(notNullValue())
//                .statusCode(200);
//    }
//
//    @Test
//    public void givenUrl_whenSuccessOnGetsResponseAndJsonHasRequiredKV_thenCorrect() {
//        get("/").then().statusCode(200);
//    }

    private final String SECURITY_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyTmFtZSI6InRlc3RpbmcxMjMiLCJwYXNzd29yZCI6IlBhc3N3b3JkQDEiLCJpYXQiOjE2Mjg1NjQyMjF9.lW8JJvJF7jKebbqPiHOBGtCAus8D9Nv1BK6IoIIMJQ4";
    private final String BASE_URI = "http://localhost:8080";
    private final String REQUEST_MAPPING_URI = "/news";
    private final String EXPECTED_NEWS_CONTENT = "Financial News";
    private final ObjectMapper mapper = new ObjectMapper();
    private Long newsID;

    @BeforeEach
    void setUp() {
        //Create a piece of news for the testing purpose.
        ServiceNewsRequestDto news = new ServiceNewsRequestDto(EXPECTED_NEWS_CONTENT);
        ServiceNewsResponseDto createdNews = newsService.create(news);
        newsID = createdNews.getId();

        mapper.registerModule(new JavaTimeModule());
    }
    @AfterEach
    void tearDown() {
        //Delete a piece of news for executing a test.
        newsService.deleteById(newsID);
    }

//    @Test
//    public void GetAllNewsTest() {
//        final int EXPECTED_STATUS_CODE = 200;
//
//        // Specify the base URL to the RESTful service
//        RestAssured.baseURI = BASE_URI + REQUEST_MAPPING_URI;
//
//        // Get the RequestSpecification of the request to be sent to the server.
//        RequestSpecification httpRequest = RestAssured.given()
////                .header("Authorization", "Bearer " + SECURITY_TOKEN)
//                .header("Content-Type", "application/json");
//
//        // Specify the method type (GET) and the parameters if any.
//        //In this case the request does not take any parameters
//        Response response = httpRequest.request(Method.GET, "");
//        //Converting the response body to string
//        String responseBodyAsString = response.asString();
//
//        // Verify the status and body of the response received from the server
//        assertEquals(EXPECTED_STATUS_CODE, response.getStatusCode());
//        assertNotNull(responseBodyAsString);
//    }
//    @Test
//    public void GetNewsByIdTest() throws JsonProcessingException {
//        final int EXPECTED_STATUS_CODE = 200;
//
//        // Specify the base URL to the RESTful service
//        RestAssured.baseURI = BASE_URI;
//        // Get the RequestSpecification of the request to be sent to the server.
//        RequestSpecification httpRequest = RestAssured.given()
////                .header("Authorization", "Bearer " + SECURITY_TOKEN)
//                .header("Content-Type", "application/json");
//        // Specify the method type (GET) and the parameters if any.
//        //In this case the request does not take any parameters
//        Response response = httpRequest.get(REQUEST_MAPPING_URI + "/" + newsID);
//        //Converting the response body to string
//        String responseBodyAsString = response.asString();
//        //Deserializing JSON response body to News object
//        ServiceNewsResponseDto news = mapper.readValue(responseBodyAsString, ServiceNewsResponseDto.class);
//        // Verify the status and body of the response received from the server
//        assertEquals(EXPECTED_STATUS_CODE, response.getStatusCode());
//        assertEquals(newsID, news.getId());
//        assertEquals(EXPECTED_NEWS_CONTENT, news.getContent());
//    }
    @Test
    public void CreateNewsTest() throws JsonProcessingException {
        final int EXPECTED_STATUS_CODE = 201;
        final String EXPECTED_CONTENT_OF_NEWLY_CREATED_NEWS = "Political News";
        // Specify the base URL to the RESTful service
        RestAssured.baseURI = BASE_URI;
        // Get the RequestSpecification of the request to be sent to the server.
        RequestSpecification httpRequest = RestAssured.given()
//                .header("Authorization", "Bearer " + SECURITY_TOKEN)
                .header("Content-Type", "application/json");
        //Create a piece of news object.
        ServiceNewsRequestDto news = new ServiceNewsRequestDto(EXPECTED_CONTENT_OF_NEWLY_CREATED_NEWS);
        //Serialize a piece of news object to json string.
        String newsString = mapper.writeValueAsString(news);
        // Send the put request to update a piece of news.
        Response response = httpRequest.body(newsString).post(REQUEST_MAPPING_URI);
        //Converting the response body to string
        String responseBodyAsString = response.asString();
        //Deserializing JSON response body to News object
        ServiceNewsResponseDto createdNews = mapper.readValue(responseBodyAsString, ServiceNewsResponseDto.class);
        // Verify the status and body of the response received from the server
        assertEquals(EXPECTED_STATUS_CODE, response.getStatusCode());
        assertNotNull(createdNews.getId());
        assertEquals(EXPECTED_CONTENT_OF_NEWLY_CREATED_NEWS, createdNews.getContent());

        //Delete the created news for cleaning testing database after the test.
        newsService.deleteById(createdNews.getId());
    }
//    @Test
//    public void UpdateNewsTest() throws JsonProcessingException {
//        final int EXPECTED_STATUS_CODE = 200;
//        final String EXPECTED_NEWS_CONTENT_AFTER_UPDATE = "Updated Financial News";
//        // Specify the base URL to the RESTful service
//        RestAssured.baseURI = BASE_URI;
//        // Get the RequestSpecification of the request to be sent to the server.
//        RequestSpecification httpRequest = RestAssured.given()
////                .header("Authorization", "Bearer " + SECURITY_TOKEN)
//                .header("Content-Type", "application/json");
//        //Create a piece of news object with new content.
//        ServiceNewsRequestDto newsWithNewContent = new ServiceNewsRequestDto(newsID, EXPECTED_NEWS_CONTENT_AFTER_UPDATE);
//
//        //Serialize a piece of news object with new content to json string.
//        String newsWithNewContentAsJson = mapper.writeValueAsString(newsWithNewContent);
//
//        // Send the put request to update a piece of news.
//        Response response = httpRequest.body(newsWithNewContentAsJson).put(REQUEST_MAPPING_URI + "/" + newsID);
//        //Converting the response body to string
//        String responseBodyAsString = response.asString();
//        //Deserializing JSON response body to News object
//        ServiceNewsResponseDto updatedNews = mapper.readValue(responseBodyAsString, ServiceNewsResponseDto.class);
//        // Verify the status and body of the response received from the server
//        assertEquals(EXPECTED_STATUS_CODE, response.getStatusCode());
//        assertEquals(newsID, updatedNews.getId());
//        assertEquals(EXPECTED_NEWS_CONTENT_AFTER_UPDATE, updatedNews.getContent());
//    }
//    @Test
//    public void DeleteNewsTest() {
//        final int EXPECTED_STATUS_CODE = 204;
//
//        // Specify the base URL to the RESTful service
//        RestAssured.baseURI = BASE_URI;
//        // Get the RequestSpecification of the request to be sent to the server.
//        RequestSpecification httpRequest = RestAssured.given()
////                .header("Authorization", "Bearer " + SECURITY_TOKEN)
//                ;
//        // Send the request to delete resource.
//        Response response = httpRequest.delete(REQUEST_MAPPING_URI + "/" + newsID);
//        // Verify the status and body of the response received from the server
//        assertEquals(EXPECTED_STATUS_CODE, response.getStatusCode());
//    }
}