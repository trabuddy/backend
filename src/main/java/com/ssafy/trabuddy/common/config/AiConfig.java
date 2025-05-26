package com.ssafy.trabuddy.common.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    ChatClient simpleChatClient(ChatClient.Builder builder) {
        return builder.build();
    }
}
