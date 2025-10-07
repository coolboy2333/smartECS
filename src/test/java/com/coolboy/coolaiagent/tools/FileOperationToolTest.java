package com.coolboy.coolaiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author coolboy2333
 * @Date 2025/10/6
 */
class FileOperationToolTest {

    @Test
    void readFile() {
        FileOperationTool tool = new FileOperationTool();
        String fileName="推荐配置.txt";
        String result = tool.readFile(fileName);
        Assertions.assertNotNull(result);

    }

    @Test
    void writeFile() {
        FileOperationTool tool = new FileOperationTool();
        String fileName="推荐配置.txt";
        String content="智能计算型 ci2k";
        String result = tool.writeFile(fileName,content);
        Assertions.assertNotNull(result);
    }
}