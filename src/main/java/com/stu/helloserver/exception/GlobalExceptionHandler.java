package com.stu.helloserver.exception;

import com.stu.helloserver.common.Result;
import com.stu.helloserver.common.ResultCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        e.printStackTrace();
        // 使用自定义业务状态码返回错误信息
        return Result.error(ResultCode.ERROR);
        // 或使用自定义消息：return Result.error("服务器异常：" + e.getMessage());
    }
}