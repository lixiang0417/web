package com.stu.helloserver.common;

public class Result<T> {
    private Integer code;  // 状态码（200=成功，500=失败）
    private String msg;    // 提示信息
    private T data;         // 泛型数据（成功时返回的业务数据）

    // 无参构造
    public Result() {}

    // 全参构造
    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // 静态成功方法（无数据）
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }

    // 静态成功方法（带数据）
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    // 静态成功方法（自定义提示 + 数据）
    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(200, msg, data);
    }

    // 静态失败方法
    public static <T> Result<T> error(String msg) {
        return new Result<>(500, msg, null);
    }

    // Getter & Setter
    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}