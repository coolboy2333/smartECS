package com.coolboy.coolaiagent.App;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author yangsheng
 * @Date 2025/9/27
 */
@SpringBootTest
@Slf4j
class SmartECSTest {
    @Resource
    private SmartECS smartECS;

    @Test
    void testChat(){
        String uuid = UUID.randomUUID().toString();
        //第一轮
        String message="你好，我叫西瓜";
        String answer= smartECS.doChat(message,uuid);
        Assertions.assertNotNull(answer);
        //第二轮
        message="我想买台服务器搭建我的个人博客";
        answer= smartECS.doChat(message,uuid);
        Assertions.assertNotNull(answer);
        //第三轮
        message="你好，我准备干什么？我刚说过的";
        answer= smartECS.doChat(message,uuid);
        Assertions.assertNotNull(answer);
    }

    @Test
    void testSafeGuard() {
        String uuid = UUID.randomUUID().toString();
        //第一轮
        String message="你好，你是TMD";
        String answer= smartECS.doChat(message,uuid);
        Assertions.assertNotNull(answer);
    }

    @Test
    void testChatWithReport() {
        String uuid = UUID.randomUUID().toString();
        String message="我想买台服务器搭建我的个人博客";
        SmartECS.SuggestReport suggestReport=smartECS.doChatWithReport(message,uuid);
        Assertions.assertNotNull(suggestReport);
    }

    @Test
    void testChatWithRAG(){
        String uuid = UUID.randomUUID().toString();
        String message="NPU卡为910B的有哪些规格";
        String answer= smartECS.doChatWithRag(message,uuid);
        Assertions.assertNotNull(answer);
    }
}