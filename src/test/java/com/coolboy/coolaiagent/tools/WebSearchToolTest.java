package com.coolboy.coolaiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author coolboy2333
 * @Date 2025/10/6
 */
@SpringBootTest
class WebSearchToolTest {
    @Value("${search-api.api-key}")
    private String searchApiKey;

    @Test
    void searchWeb() {
        WebSearchTool tool = new WebSearchTool(searchApiKey);
        String query = "移动云 裸金属服务器";
        String result = tool.searchWeb(query);
        assertNotNull(result);
    }
}