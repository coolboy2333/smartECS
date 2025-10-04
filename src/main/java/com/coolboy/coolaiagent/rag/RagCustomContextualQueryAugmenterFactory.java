package com.coolboy.coolaiagent.rag;

import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;

/**
 * @Author coolboy2333
 * @Date 2025/10/4
 */
public class RagCustomContextualQueryAugmenterFactory {
    public static ContextualQueryAugmenter createInstance() {
        PromptTemplate emptyContextPromptTemplate = new PromptTemplate("""
                你应该输出下面的内容：
                抱歉，我只能回答移动云ECS（云主机、裸金属服务器）相关的问题，别的没办法帮到您哦，
                有问题可以联系移动云客服 https://ecloud.10086.cn/
                """);
        return ContextualQueryAugmenter.builder()
                .allowEmptyContext(false)
                .emptyContextPromptTemplate(emptyContextPromptTemplate)
                .build();
    }
}
