package com.stu.helloserver.exception;

import com.stu.helloserver.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice  // 拦截所有 @Controller 的异常
public class GlobalExceptionHandler {

    // 捕获所有 Exception 类型的异常
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        // 打印异常栈（方便后端排查问题）
        e.printStackTrace();
        // 返回统一格式的 JSON 错误响应
        return Result.error("服务器异常：" + e.getMessage());
    }
}