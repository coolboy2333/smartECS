package com.coolboy.coolaiagent.App;

import com.coolboy.coolaiagent.advisor.MyLoggerAdvisor;
import com.coolboy.coolaiagent.advisor.MySafeGuardAdvisor;
import com.coolboy.coolaiagent.rag.QueryRewriter;
import com.coolboy.coolaiagent.rag.RagCustomAdvisorFactory;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author coolboy2333
 * @Date 2025/9/27
 */
@Component
@Slf4j
public class SmartECS {
    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = "你是一位经验丰富的云计算顾问，专注于移动云ECS（云主机、裸金属服务器）产品的咨询和优化。你的目标是帮助用户找到最适合他们需求的ECS配置，解决他们在订购和使用过程中遇到的问题。请遵循以下指南与用户进行交流：\n" +
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

    private static final String tag = "规格";

    @Resource
    private VectorStore myVectorStore;

    @Resource
    private QueryRewriter queryRewriter;

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
                        new MyLoggerAdvisor(),
                        new MySafeGuardAdvisor(List.of("TMD", "黄色"))
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

    record SuggestReport(String title, List<String> suggestions) {
    }

    public SuggestReport doChatWithReport(String message, String chatId) {
        SuggestReport suggestReport = chatClient
                .prompt()
                .system(SYSTEM_PROMPT + "每次对话后都要生成结果，标题为｛用户名｝的使用建议，内容为建议列表")
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                .call()
                .entity(SuggestReport.class);
        log.info("suggestReport: {}", suggestReport);
        return suggestReport;
    }

    public String doChatWithRag(String message, String chatId) {
        // 查询重写
        String rewrittenMessage = queryRewriter.doQueryRewrite(message);
        ChatResponse response = chatClient
                .prompt()
                .user(rewrittenMessage)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                //应用 RAG 知识库问答
                .advisors(new QuestionAnswerAdvisor(myVectorStore))
                //TODO 查询矢量数据库
                //应用自定义的 RAG 检索增强服务（文档查询器 + 上下文增强器）
                .advisors(RagCustomAdvisorFactory.createRagCustomAdvisorFactory(myVectorStore, tag))
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

    @Resource
    private ToolCallback[] allTools;

    public String doChatWithTools(String message, String chatId) {
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                // 开启日志，便于观察效果
                .advisors(new MyLoggerAdvisor())
                .toolCallbacks(allTools)
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }


}
