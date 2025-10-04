package com.coolboy.coolaiagent.advisor;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

import reactor.core.publisher.Flux;

/**
 * @Author coolboy2333
 * @Date 2025/10/1
 */
public class MySafeGuardAdvisor implements CallAdvisor, StreamAdvisor {
    private static final String DEFAULT_FAILURE_RESPONSE = "您输入的内容含敏感内容，我们可以聊点别的吗？";
    private static final int DEFAULT_ORDER = 0;
    private final String failureResponse;
    private final List<String> sensitiveWords;
    private final int order;

    public MySafeGuardAdvisor(List<String> sensitiveWords) {
        this(sensitiveWords, "您输入的内容含敏感内容，我们可以聊点别的吗？", DEFAULT_ORDER);
    }

    public MySafeGuardAdvisor(List<String> sensitiveWords, String failureResponse, int order) {
        Assert.notNull(sensitiveWords, "敏感词不可为空!");
        Assert.notNull(failureResponse, "错误返回不可为空!");
        this.sensitiveWords = sensitiveWords;
        this.failureResponse = failureResponse;
        this.order = order;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        return !CollectionUtils.isEmpty(this.sensitiveWords) && this.sensitiveWords.stream().anyMatch((w) -> chatClientRequest.prompt().getContents().contains(w)) ? this.createFailureResponse(chatClientRequest) : callAdvisorChain.nextCall(chatClientRequest);
    }

    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {
        return !CollectionUtils.isEmpty(this.sensitiveWords) && this.sensitiveWords.stream().anyMatch((w) -> chatClientRequest.prompt().getContents().contains(w)) ? Flux.just(this.createFailureResponse(chatClientRequest)) : streamAdvisorChain.nextStream(chatClientRequest);
    }

    private ChatClientResponse createFailureResponse(ChatClientRequest chatClientRequest) {
        return ChatClientResponse.builder().chatResponse(ChatResponse.builder().generations(List.of(new Generation(new AssistantMessage(this.failureResponse)))).build()).context(Map.copyOf(chatClientRequest.context())).build();
    }
}
