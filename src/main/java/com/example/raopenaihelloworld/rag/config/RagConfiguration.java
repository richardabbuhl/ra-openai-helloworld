package com.example.raopenaihelloworld.rag.config;

import com.example.raopenaihelloworld.rag.RagService;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RagConfiguration {

	@Bean
	public RagService ragService(ChatClient chatClient, EmbeddingClient embeddingClient) {
		return new RagService(chatClient, embeddingClient);
	}

}
