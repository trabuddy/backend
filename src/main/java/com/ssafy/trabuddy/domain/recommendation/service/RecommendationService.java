package com.ssafy.trabuddy.domain.recommendation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationService {
    public record Recommendation(String name, String reason) {
    }

    private static final List<String> themes
            = List.of("자유", "힐링/휴식", "먹방", "혼자", "무계획", "레저/스포츠");

    @Qualifier("simpleChatClient")
    private final ChatClient simpleChatClient;

    public List<Recommendation> recommend(int count, int people, LocalDateTime startDate, LocalDateTime endDate, int themeId) {
        PromptTemplate pt = new PromptTemplate("""
                {people}명의 사람들이 {startDate}부터 {endDate}까지 여행하려고 합니다.
                한국의 여행지를 {count}개 추천해주세요. 여행 테마는 {theme}입니다.
                여행지 추천은 여행지 이름(name)과 추천 이유(reason)가 필요합니다.
                여행지 이름은 부가 설명 없이 구체적인 지명이나 장소를 적어주세요.
                추천 이유는 30자 내외로 간결하게, 해당 여행지를 모르는 사람에게 설명하듯이 친절하게 동사 형태로 적어주세요.
                """);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedStartDate = startDate.format(formatter);
        String formattedEndDate = endDate.format(formatter);
        String theme = themeId < themes.size() ? themes.get(themeId) : themes.get(0);

        Prompt prompt = pt.create(Map.of(
                "count", count,
                "people", people,
                "startDate", formattedStartDate,
                "endDate", formattedEndDate,
                "theme", theme));

        return simpleChatClient.prompt(prompt)
                .call()
                .entity(new ParameterizedTypeReference<List<Recommendation>>() {
                });
    }
}
