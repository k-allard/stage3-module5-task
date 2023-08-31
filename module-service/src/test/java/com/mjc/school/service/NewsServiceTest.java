//package com.mjc.school.service;
//
//import com.mjc.school.repository.impl.NewsRepository;
//import com.mjc.school.repository.model.NewsModel;
//import com.mjc.school.service.dto.ServiceNewsRequestDto;
//import com.mjc.school.service.dto.ServiceNewsResponseDto;
//import com.mjc.school.service.impl.NewsService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.ArgumentMatchers.any;
//
//@ExtendWith(MockitoExtension.class)
//class NewsServiceTest {
//    private static final long INITIAL_NUMBER_OF_NEWS = 5;
//    private static final long VALID_NEWS_ID = 3L;
//    private static final long VALID_AUTHOR_ID = 2L;
//    private static final String VALID_NEWS_TITLE = "VALID TITLE";
//    private static final String VALID_NEWS_CONTENT = "Valid content.";
//    private final List<NewsModel> newsList = new ArrayList<>();
//    NewsService newsService;
//    @Mock
//    private NewsRepository newsRepository;
//
//    @BeforeEach
//    void init() {
//        newsService = new NewsService(newsRepository);
//
//        newsList.add(new NewsModel(1L, "FINANCIAL INSTITUTIONS", "One man's drastic 400 pound weight loss spurred", LocalDateTime.now(), LocalDateTime.now(), 1L));
//        newsList.add(new NewsModel(2L, "COMMERCE AND TRADE", "An inspiring couple recreating their wedding", LocalDateTime.now(), LocalDateTime.now(), 2L));
//        newsList.add(new NewsModel(3L, "UNIFORM COMMERCIAL CODE", "The worst flood in India in 100 years brings Twitter", LocalDateTime.now(), LocalDateTime.now(), 3L));
//        newsList.add(new NewsModel(4L, "CONSERVATION", "A Nigerian boy solves a 30-year math equation", LocalDateTime.now(), LocalDateTime.now(), 4L));
//        newsList.add(new NewsModel(5L, "CORPORATIONS", "My doctor ordered a $6,000 treatment machine", LocalDateTime.now(), LocalDateTime.now(), 5L));
//    }
//
//    @Test
//    @DisplayName("getAllNews() returns initial list of news")
//    void getAllNews() {
//        Mockito.when(newsRepository.readAll())
//                .thenReturn(newsList);
//        List<ServiceNewsResponseDto> list = newsService.readAll();
//        assertEquals(INITIAL_NUMBER_OF_NEWS, list.size());
//    }
//
//    @Test
//    @DisplayName("getNewsById() returns correct news")
//    void getNewsByValidId() {
//        Mockito.when(newsRepository.readById(VALID_NEWS_ID))
//                .thenReturn(Optional.ofNullable(
//                        newsList.get(newsList.indexOf(new NewsModel(VALID_NEWS_ID))))
//                );
//        ServiceNewsResponseDto news = newsService.readById(VALID_NEWS_ID);
//        assertEquals(VALID_NEWS_ID, news.getId());
//    }
//
//    @Test
//    @DisplayName("createNews() returns new news")
//    void createValidNewsAndCheckResponse() {
//        Mockito
//                .when(
//                        newsRepository.create(any(NewsModel.class)))
//                .thenReturn(
//                        new NewsModel(6L, VALID_NEWS_TITLE, VALID_NEWS_CONTENT, LocalDateTime.now(),
//                                LocalDateTime.now(), VALID_AUTHOR_ID));
//
//
//        ServiceNewsResponseDto response = newsService.create(
//                new ServiceNewsRequestDto(null, VALID_NEWS_TITLE, VALID_NEWS_CONTENT, VALID_AUTHOR_ID)
//        );
//        assertEquals(VALID_NEWS_TITLE, response.getTitle());
//        assertEquals(VALID_NEWS_CONTENT, response.getContent());
//        assertEquals(VALID_AUTHOR_ID, response.getAuthorId());
//        assertNotNull(response.getId());
//        assertNotNull(response.getCreateDate());
//        assertNotNull(response.getLastUpdateDate());
//    }
//
//    @Test
//    @DisplayName("updateNews() returns updated news")
//    void updateValidNewsAndCheckResponse() {
//        Mockito
//                .when(newsRepository.update(any(NewsModel.class)))
//                .thenReturn(new NewsModel(
//                        VALID_NEWS_ID, VALID_NEWS_TITLE, VALID_NEWS_CONTENT, LocalDateTime.now(),
//                        LocalDateTime.now(), VALID_AUTHOR_ID));
//
//        ServiceNewsResponseDto response = newsService.update(
//                new ServiceNewsRequestDto(VALID_NEWS_ID, VALID_NEWS_TITLE, VALID_NEWS_CONTENT, VALID_AUTHOR_ID)
//        );
//        assertEquals(VALID_NEWS_ID, response.getId());
//        assertEquals(VALID_NEWS_TITLE, response.getTitle());
//        assertEquals(VALID_NEWS_CONTENT, response.getContent());
//        assertEquals(VALID_AUTHOR_ID, response.getAuthorId());
//    }
//
//    @Test
//    @DisplayName("removeNews() return true if id existed")
//    void removeNewsWithValidId() {
//        Mockito.when(newsRepository.deleteById(VALID_NEWS_ID))
//                .thenReturn(true);
//        assertTrue(newsService.deleteById(VALID_NEWS_ID));
//    }
//}
