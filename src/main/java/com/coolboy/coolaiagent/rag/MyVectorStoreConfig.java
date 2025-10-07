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
 * @Author coolboy2333
 * @Date 2025/10/3
 */
@Configuration
public class MyVectorStoreConfig {
    @Resource
    private DocumentLoader documentLoader;

    @Resource
    private DocumentEnricher documentEnricher;

    @Bean
    VectorStore myVectorStore(EmbeddingModel embeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
        //加载文档
        List<Document> documents = documentLoader.loadPdfs();
        //自动补充关键词元数据
//        List<Document> enrichedDocuments = documentEnricher.enrichDocumentsByKeyword(documents);
        simpleVectorStore.add(documents);
        return simpleVectorStore;
    }
}
