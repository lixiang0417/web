package com.stu.helloserver.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.stu.helloserver.service.ChatService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class ChatServiceImpl implements ChatService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${dashscope.api-key}")
    private String apiKey;

    @Value("${dashscope.model:qwen3.7-plus}")
    private String model;

    @Override
    public String chat(String message) {
        // 构造请求体
        JSONObject body = new JSONObject();
        body.put("model", model);

        JSONArray messages = new JSONArray();
        JSONObject userMsg = new JSONObject();
        userMsg.put("role", "user");
        userMsg.put("content", message);
        messages.add(userMsg);
        body.put("messages", messages);

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<String> request = new HttpEntity<>(body.toJSONString(), headers);

        // 调用 DashScope API
        ResponseEntity<String> response = restTemplate.exchange(
                "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions",
                HttpMethod.POST,
                request,
                String.class
        );

        // 解析返回结果
        JSONObject respJson = JSON.parseObject(response.getBody());
        JSONArray choices = respJson.getJSONArray("choices");
        if (choices != null && !choices.isEmpty()) {
            JSONObject choice = choices.getJSONObject(0);
            JSONObject innerMsg = choice.getJSONObject("message");
            if (innerMsg != null) {
                return innerMsg.getString("content");
            }
        }
        return "抱歉，我暂时无法回答这个问题。";
    }
}
