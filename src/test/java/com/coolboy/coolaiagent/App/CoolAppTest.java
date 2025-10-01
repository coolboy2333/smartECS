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
class CoolAppTest {
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
    void testLog() {
        log.info("测试");
    }
}