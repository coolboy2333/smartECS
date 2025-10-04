package com.coolboy.coolaiagent.demo.invoke;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * @Author coolboy2333
 * @Date 2025/9/26
 */
public class HttpAiInvoke {

    public static void main(String[] args) {
        // 替换为你的DashScope API密钥
        String dashScopeApiKey = TestApiKey.API_KEY;

        // 创建消息数组
        JSONArray messages = JSONUtil.createArray();
        messages.add(JSONUtil.createObj().put("role", "system").put("content", "You are a helpful assistant."));
        messages.add(JSONUtil.createObj().put("role", "user").put("content", "你是谁？"));

        // 构建请求数据
        JSONObject requestBody = JSONUtil.createObj()
                .put("model", "qwen-plus")
                .put("messages", messages);

        // 发送POST请求
        HttpResponse response = HttpRequest.post("https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions")
                .header("Authorization", "Bearer " + dashScopeApiKey)
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .execute();

        // 处理响应
        if (response.isOk()) { // 检查HTTP状态码是否为200
            System.out.println("响应内容:");
            System.out.println(response.body()); // 打印响应体
        } else {
            System.err.println("请求失败，状态码: " + response.getStatus());
            System.err.println("响应内容:");
            System.err.println(response.body());
        }
    }
}

