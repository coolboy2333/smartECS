package com.coolboy.coolaiagent.agent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.annotation.Resource;

/**
 * @Author coolboy2333
 * @Date 2025/10/17
 */
@SpringBootTest
class CoolManusTest {
    @Resource
    private CoolManus coolManus;

    @Test
    void run() {
        String userPrompt = """
                我的公司在无锡，请帮我找到离我最近的资源池，我要搭建大型交友网站，
                请推荐服务器配置，并估算费用，并提供搭建网站的详细步骤，要求图文并茂，
                最后以PDF格式输出""";
        String answer = coolManus.run(userPrompt);
        Assertions.assertNotNull(answer);
    }
}