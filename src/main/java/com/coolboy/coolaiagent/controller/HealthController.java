package com.coolboy.coolaiagent.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author yangsheng
 * @Date 2025/9/26
 */
@RestController
@RequestMapping("/health")
public class HealthController {
    @GetMapping
    public String healthCheck() {
        return "OK";
    }
}
