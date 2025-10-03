package com.coolboy.coolaiagent.rag;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author yangsheng
 * @Date 2025/10/2
 */
@Component
@Slf4j
public class DocumentLoader {
    private final ResourcePatternResolver resourcePatternResolver;

    DocumentLoader(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }

    /**
     * 加载多篇PDF文档
     * */
    public List<Document> loadPdfs(){
        List<Document> documents = new ArrayList<Document>();
        try {
            Resource[] resources=resourcePatternResolver.getResources("classpath:document/*.pdf");
            for (Resource resource : resources) {
                String filename = resource.getFilename();
                PdfDocumentReaderConfig config=PdfDocumentReaderConfig.builder()
                        .withPageTopMargin(0)
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                .withNumberOfTopTextLinesToDelete(0)
                                .build())
                        .withPagesPerDocument(1)
                        .build();
                PagePdfDocumentReader pdfReader=new PagePdfDocumentReader(resource,config);
                documents.addAll(pdfReader.get());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return documents;
    }
}
