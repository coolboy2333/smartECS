package com.coolboy.coolaiagent.App;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author coolboy2333
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
        String message="我想买一台裸金属服务器用于模型训推，有什么规格可以推荐？";
        String answer= smartECS.doChatWithRag(message,uuid);
        Assertions.assertNotNull(answer);
    }


    @Test
    void doChatWithTools() {
        // 测试联网搜索问题的答案
        testMessage("想搭建一个政府网站，推荐两款国产化服务器？");

        // 测试网页抓取：最佳实践分析
        testMessage("想看看服务器的最佳实践，看看移动云（https://ecloud.10086.cn/）有哪些推荐，列举两个即可");

        // 测试资源下载：图片下载
        testMessage("下载一张国产化服务器图片为png文件");

        // 测试文件操作：保存用户档案
        testMessage("搭建一个政府网站，推荐服务配置，并将服务器配置为txt文件");

        // 测试 PDF 生成
        testMessage("搭建一个政府网站，推荐服务配置，生成一份‘服务器配置’PDF，包含CPU、内存、磁盘等信息");

        // 测试终端操作：执行代码
        testMessage("执行 dir 命令，看下当前目录有什么内容");
    }

    private void testMessage(String message) {
        String chatId = UUID.randomUUID().toString();
        String answer = smartECS.doChatWithTools(message, chatId);
        Assertions.assertNotNull(answer);
    }
}