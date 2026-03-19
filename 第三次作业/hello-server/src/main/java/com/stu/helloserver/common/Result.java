package com.stu.helloserver.common;

public class Result<T> {
    private Integer code;
    private String msg;
    private T data;

    // 无参构造
    public Result() {}

    // 静态工厂方法：成功回调（带数据）
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.code = ResultCode.SUCCESS.getCode();
        result.msg = ResultCode.SUCCESS.getMsg();
        result.data = data;
        return result;
    }

    // 静态工厂方法：成功回调（无数据）
    public static <T> Result<T> success() {
        return success(null);
    }

    // 静态工厂方法：失败回调（使用枚举状态码）
    public static <T> Result<T> error(ResultCode resultCode) {
        Result<T> result = new Result<>();
        result.code = resultCode.getCode();
        result.msg = resultCode.getMsg();
        result.data = null;
        return result;
    }

    // 静态工厂方法：失败回调（自定义消息）
    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.code = ResultCode.ERROR.getCode();
        result.msg = msg;
        result.data = null;
        return result;
    }

    // Getter & Setter（IDEA 可自动生成）
    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}