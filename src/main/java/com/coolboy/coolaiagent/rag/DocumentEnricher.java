package com.coolboy.coolaiagent.rag;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.model.transformer.KeywordMetadataEnricher;
import org.springframework.stereotype.Component;

import java.util.List;

import jakarta.annotation.Resource;

/**
 * @Author coolboy2333
 * @Date 2025/10/4
 * 文档元数据增强器
 */
@Component
public class DocumentEnricher {
    @Resource
    private ChatModel chatModel;

    /**
     * 关键词元数据增强器
     * @param documents
     * @return
     */
    public List<Document> enrichDocumentsByKeyword(List<Document> documents) {
        KeywordMetadataEnricher keywordMetadataEnricher = new KeywordMetadataEnricher(chatModel, 5);
        return  keywordMetadataEnricher.apply(documents);
    }
}
