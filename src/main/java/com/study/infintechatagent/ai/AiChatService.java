package com.study.infintechatagent.ai;

import com.study.infintechatagent.config.McpToolConfig;
import com.study.infintechatagent.tool.EmailTool;
import com.study.infintechatagent.tool.RagTool;
import com.study.infintechatagent.tool.TimeTool;
import dev.langchain4j.community.store.memory.chat.redis.RedisChatMemoryStore;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AiChatService {

    @Resource
    private ChatModel chatModel;

    @Resource
    private StreamingChatModel streamingChatModel;

//    @Resource
//    private EmbeddingStore<TextSegment> embeddingStore;

    @Resource
    private McpToolProvider mcpToolProvider;

    @Resource
    private RedisChatMemoryStore redisChatMemoryStore;

    @Resource
    private ContentRetriever contentRetriever;

    @Resource
    private RagTool ragTool;

    @Resource
    private EmailTool emailTool;

    @Bean
    public AiChat aiChat() {

//        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);

//        List<Document> documents = FileSystemDocumentLoader.loadDocuments("src/main/resources/docs");
//        EmbeddingStoreIngestor.ingest(documents,embeddingStore);

        return AiServices.builder(AiChat.class)
                .chatModel(chatModel)
                .streamingChatModel(streamingChatModel)
               .contentRetriever(contentRetriever)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory
                        .builder()
                        .id(memoryId)
                        .chatMemoryStore(redisChatMemoryStore)
                        .maxMessages(20)
                        .build())
                .tools(new TimeTool(),ragTool,emailTool)
                .toolProvider(mcpToolProvider)
                .build();
    }

}
