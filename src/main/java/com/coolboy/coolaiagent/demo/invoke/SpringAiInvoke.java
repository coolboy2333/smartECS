package com.coolboy.coolaiagent.demo.invoke;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;


import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * @Author yangsheng
 * @Date 2025/9/26
 */

// 取消注释即可在 SpringBoot 项目启动时执行
//@Component
public class SpringAiInvoke implements CommandLineRunner {

    @Resource
    private ChatModel dashscopeChatModel;

    @Override
    public void run(String... args) throws Exception {
        AssistantMessage output = dashscopeChatModel.call(new Prompt("你好，我是虾皮"))
                .getResult()
                .getOutput();
        System.out.println(output.getText());
    }
}

