package com.coolboy.coolaiagent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author coolboy2333
 * @Date 2025/10/6
 */
class ResourceDownloadToolTest {

    @Test
    void downloadResource() {
        ResourceDownloadTool tool = new ResourceDownloadTool();
        String url = "https://ecloud.eos-guangzhou-1.cmecloud.cn/op-portal-static/9.4.1_h2/static/ecloud-logo-2023.835f0962.png";
        String fileName = "logo.png";
        String result = tool.downloadResource(url, fileName);
        assertNotNull(result);
    }
}