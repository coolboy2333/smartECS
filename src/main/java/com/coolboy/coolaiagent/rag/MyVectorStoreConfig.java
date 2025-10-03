package com.coolboy.coolaiagent.rag;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import jakarta.annotation.Resource;

/**
 * @Author yangsheng
 * @Date 2025/10/3
 */
@Configuration
public class MyVectorStoreConfig {
    @Resource
    private DocumentLoader documentLoader;

    @Bean
    VectorStore myVectorStore(EmbeddingModel embeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
        //加载文档
        List<Document> documents = documentLoader.loadPdfs();
        simpleVectorStore.add(documents);
        return simpleVectorStore;
    }
}
