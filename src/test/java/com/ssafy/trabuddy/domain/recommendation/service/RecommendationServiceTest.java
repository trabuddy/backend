package com.ssafy.trabuddy.domain.recommendation.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@Slf4j
@SpringBootTest
public class RecommendationServiceTest {

    @Autowired
    RecommendationService recommendationService;

    @Test
    void recommendTest() throws Exception {
        var startDate = LocalDateTime.now();
        var endDate = LocalDateTime.now().plusDays(7);

        var rec = recommendationService.recommend(
                5,
                2,
                startDate,
                endDate,
                2
        );
        log.info("content: {}", rec);
    }
}