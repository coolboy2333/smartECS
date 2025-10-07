package com.coolboy.coolaiagent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author coolboy2333
 * @Date 2025/10/6
 */
class PDFGenerationToolTest {

    @Test
    void generatePDF() {
        PDFGenerationTool tool = new PDFGenerationTool();
        String fileName = "裸金属服务器.pdf";
        String content = "裸金属服务器 BMS (Bare Metal Server)是一种可弹性扩展的专属计算服务，具有与传统物理机无差异的计算性能和安全物理隔离的特点，满足企业关键业务对高性能、安全性和稳定性的使用诉求。\n" +
                "全线兼容移动云产品，分钟级交付，助力您的核心业务快速上云。";
        String result = tool.generatePDF(fileName, content);
        assertNotNull(result);
    }
}