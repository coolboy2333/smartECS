package com.coolboy.coolaiagent.controller;

import com.coolboy.coolaiagent.App.SmartECS;
import com.coolboy.coolaiagent.agent.CoolManus;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

import jakarta.annotation.Resource;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
public class AiController {

    @Resource
    private SmartECS smartECS;

    @Resource
    private ToolCallback[] allTools;

    @Resource
    private ChatModel chatModel;

    /**
     * 同步调用SmartECS应用
     *
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping("/smart_ecs/chat/sync")
    public String doChatWithSmartECSSync(String message, String chatId) {
        return smartECS.doChat(message, chatId);
    }

    /**
     * SSE流式调用SmartECS
     * 返回Flux响应式对象，通过MediaType来控制
     *
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping(value = "/smart_ecs/chat/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithSmartECSSSE(String message, String chatId) {
        return smartECS.doChatByStream(message, chatId);
    }

    /**
     * ServerSentEvent流式调用SmartECS
     * 返回Flux响应式对象，并设置ServerSentEvent泛型对象，省略MediaType
     *
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping(value = "/smart_ecs/chat/server_sent_event")
    public Flux<ServerSentEvent<String>> doChatWithSmartECSServerSentEvent(String message, String chatId) {
        return smartECS.doChatByStream(message, chatId)
                .map(chunk -> ServerSentEvent.<String>builder()
                        .data(chunk)
                        .build());
    }

    /**
     * SseEmitter流式调用SmartECS
     * 通过send方法持续向SseEmitter发送消息
     *
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping(value = "/smart_ecs/chat/sse_emitter")
    public SseEmitter doChatWithSmartECSSseEmitter(String message, String chatId) {
        // 创建一个超时时间较长的 SseEmitter
        SseEmitter sseEmitter = new SseEmitter(180000L); // 3 分钟超时
        // 获取 Flux 响应式数据流并且直接通过订阅推送给 SseEmitter
        smartECS.doChatByStream(message, chatId)
                .subscribe(chunk -> {
                    try {
                        sseEmitter.send(chunk);
                    } catch (IOException e) {
                        sseEmitter.completeWithError(e);
                    }
                }, sseEmitter::completeWithError, sseEmitter::complete);
        // 返回
        return sseEmitter;
    }

    /**
     * 流式调用 Manus 超级智能体
     *
     * @param message
     * @return
     */
    @GetMapping(value = "/manus/chat")
    public SseEmitter doChatWithManus(String message) {
        CoolManus coolManus = new CoolManus(allTools, chatModel);
        return coolManus.runStream(message);
    }
}
