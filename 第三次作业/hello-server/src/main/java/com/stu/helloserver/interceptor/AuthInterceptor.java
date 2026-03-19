package com.stu.helloserver.interceptor;

import com.stu.helloserver.common.Result;
import com.stu.helloserver.common.ResultCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取本次请求的 HTTP 动词和具体路径
        String method = request.getMethod();
        String uri = request.getRequestURI();

        // 2. 手写细粒度放行规则
        // 规则 A：POST 请求且路径精确等于 "/api/users" → 放行（允许注册）
        boolean isCreateUser = "POST".equalsIgnoreCase(method) && "/api/users".equals(uri);
        // 规则 B：GET 请求且路径以 "/api/users/" 开头 → 放行（允许查看用户详情）
        boolean isGetUser = "GET".equalsIgnoreCase(method) && uri.startsWith("/api/users/");

        // 只要满足任一合法公开规则，直接放行，无需校验 Token
        if (isCreateUser || isGetUser) {
            return true;
        }

        // 3. 执行严格的 Token 校验（针对 DELETE、PUT 等敏感操作）
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            response.setContentType("application/json;charset=UTF-8");
            // 构造统一格式的 401 错误 JSON
            Result<Object> errorResult = Result.error(ResultCode.TOKEN_INVALID);
            response.getWriter().write(new ObjectMapper().writeValueAsString(errorResult));
            return false; // 拦截请求
        }

        // Token 存在，放行请求
        return true;
    }
}