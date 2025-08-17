package com.tsm.api.common;



import java.io.Serializable;

/**
 * 统一响应结果类
 * 
 * @author TSM Team
 * @since 1.0.0
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成功状态码
     */
    public static final int SUCCESS_CODE = 200;

    /**
     * 失败状态码
     */
    public static final int ERROR_CODE = 500;

    /**
     * 未授权状态码
     */
    public static final int UNAUTHORIZED_CODE = 401;

    /**
     * 禁止访问状态码
     */
    public static final int FORBIDDEN_CODE = 403;

    /**
     * 状态码
     */
    private int code;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    /**
     * 时间戳
     */
    private long timestamp;

    public Result() {
        this.timestamp = System.currentTimeMillis();
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 成功响应
     */
    public static <T> Result<T> success() {
        return new Result<>(SUCCESS_CODE, "操作成功");
    }

    /**
     * 成功响应
     */
    public static <T> Result<T> success(String message) {
        return new Result<>(SUCCESS_CODE, message);
    }

    /**
     * 成功响应
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(SUCCESS_CODE, "操作成功", data);
    }

    /**
     * 成功响应
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(SUCCESS_CODE, message, data);
    }

    /**
     * 失败响应
     */
    public static <T> Result<T> error() {
        return new Result<>(ERROR_CODE, "操作失败");
    }

    /**
     * 失败响应
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(ERROR_CODE, message);
    }

    /**
     * 失败响应
     */
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message);
    }

    /**
     * 未授权响应
     */
    public static <T> Result<T> unauthorized() {
        return new Result<>(UNAUTHORIZED_CODE, "未授权访问");
    }

    /**
     * 未授权响应
     */
    public static <T> Result<T> unauthorized(String message) {
        return new Result<>(UNAUTHORIZED_CODE, message);
    }

    /**
     * 禁止访问响应
     */
    public static <T> Result<T> forbidden() {
        return new Result<>(FORBIDDEN_CODE, "禁止访问");
    }

    /**
     * 禁止访问响应
     */
    public static <T> Result<T> forbidden(String message) {
        return new Result<>(FORBIDDEN_CODE, message);
    }

    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return SUCCESS_CODE == this.code;
    }

    /**
     * 判断是否失败
     */
    public boolean isError() {
        return !isSuccess();
    }

    // Getter and Setter methods
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", timestamp=" + timestamp +
                '}';
    }
}