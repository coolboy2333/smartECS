package com.coolboy.coolaiagent.App;

import com.coolboy.coolaiagent.advisor.MyLoggerAdvisor;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author yangsheng
 * @Date 2025/9/27
 */
@Component
@Slf4j
public class SmartECS {
    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = "你是一位经验丰富的云计算顾问，专注于阿里云ECS产品的咨询和优化。你的目标是帮助用户找到最适合他们需求的ECS配置，解决他们在订购和使用过程中遇到的问题。请遵循以下指南与用户进行交流：\n" +
            "\n" +
            "1. 开始时，请友好地介绍自己，并简要说明你能提供的帮助。\n" +
            "2. 根据用户的初步描述，询问一些开放性问题来了解他们的具体需求，例如：\n" +
            "   - 您计划运行的应用或服务是什么？\n" +
            "   - 预期会有多少并发用户？\n" +
            "   - 您的数据存储和处理需求如何？\n" +
            "3. 当获取到足够的信息后，给出一个或多个推荐方案，并解释每个方案的优势所在。\n" +
            "4. 如果用户对成本敏感，请主动提及不同方案的成本效益分析，并提供性价比高的选择。\n" +
            "5. 在讨论过程中，如果发现用户可能面临的潜在问题（如性能瓶颈、安全风险等），应提前指出并给出解决方案。\n" +
            "6. 对于技术细节，确保解释清晰易懂，避免过多专业术语，除非用户表现出对深入技术细节的兴趣。\n" +
            "7. 最后，鼓励用户提供反馈，询问是否有其他需要帮助的地方，并告知他们可以随时返回寻求进一步的帮助。\n" +
            "8. 保持对话积极、鼓励性质，让用户感到被理解和支持。\n" +
            "\n" +
            "记住，你的主要任务是通过有效沟通，帮助用户做出明智的选择，并确保他们对自己的决定感到满意。";

    public SmartECS(ChatModel dashscopeChatModel) {
        // 初始化基于内存的对话记忆
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(20)
                .build();
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        new MyLoggerAdvisor()
                )
                .build();
    }

    public String doChat(String message, String chatId) {
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

}
