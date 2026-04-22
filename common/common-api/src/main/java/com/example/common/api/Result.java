package com.example.common.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer code;
    private String message;
    private T data;
    private Boolean success;
    private String requestId;

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        result.setSuccess(true);
        result.setRequestId(java.util.UUID.randomUUID().toString());
        return result;
    }

    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(null);
        result.setSuccess(false);
        result.setRequestId(java.util.UUID.randomUUID().toString());
        return result;
    }

    public static <T> Result<T> error(String message) {
        return error(500, message);
    }

    public boolean isSuccess() {
        return success != null && success;
    }
}
